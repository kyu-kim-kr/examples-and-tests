package rpg;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

class RpgGame_frame extends JFrame implements Runnable, //쓰레드 인터페이스
        KeyListener {

    boolean keyUp = false;
    boolean keyDown = false;
    boolean keyLeft = false;
    boolean keyRight = false;
    boolean playerMove = false;

    Toolkit tk = Toolkit.getDefaultToolkit(); //이미지불러오기위해 툴킷 인스턴스 생성

    Image img = tk.getImage("rpg.png");
    //위에 이미지 이름이 바로 rpg.png입니다. 이미지를 불러옵니다
    Image buffimg;// 더블버퍼링용 입니다.
    Graphics gc;

    Thread th;

    int x, y; // 케릭터의 현재 좌표를 받을 변수
    int cnt; //무한 루프를 카운터 하기 위한 변수
    int moveStatus; //케릭터가 어디를 바라보는지 방향을 받을 변수

    RpgGame_frame(){
        setTitle("테스트");
        setSize(800, 600);
        init();
        start();



        setResizable(false);
        setVisible(true);
    }

    public void init(){
        x = 100;
        y = 100;

        moveStatus = 2;
//케릭터가 시작할때 바라보는 방향은 아래쪽입니다.
// 0 : 위쪽, 1 : 오른쪽, 2 : 아래쪽, 3 : 왼쪽

    }

    public void start(){ // 기본적인 명령처리
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //창닫아주는거
        addKeyListener(this); // 키보드 이벤트처리를 위한
        th = new Thread(this); // 새로운 스레드만들고
        th.start(); // 스타트하면 run을 실행
    }

    public void update(Graphics g){
//더블 버퍼링을 이용해 버퍼에 그려진것을 가져옵니다.
        DrawImg();

        g.drawImage(buffimg, 0, 0, this);
    }
    public void paint(Graphics g){ //더블버퍼링을 사용합니다.
        buffimg = createImage(800, 600);
        gc = buffimg.getGraphics();
        update(g);
    }

    public void run(){ // 스레드 메소드, 무한 루프
        while(true){
            try{
                keyProcess(); // 메소드실행 (이거 실행되기전에 AWT쓰레드가 실행됨)
                // keyprocess가 실행되기전에 keyListener가 실행되면서
                // keyPressed 의해서 key변수의 값이 true로 바뀜
                // keyProcess() 가 실행됨
                repaint();

                Thread.sleep(20);
                cnt++;

            }catch(Exception e){}
        }
    }


    public void keyPressed(KeyEvent e) {

        switch(e.getKeyCode()){
            case KeyEvent.VK_LEFT :
                keyLeft = true;
                break;
            case KeyEvent.VK_RIGHT :
                keyRight = true;
                break;
            case KeyEvent.VK_UP :
                keyUp = true;
                break;
            case KeyEvent.VK_DOWN :
                keyDown = true;
                break;
        }
    }

    public void keyReleased(KeyEvent e) {
        switch(e.getKeyCode()){
            case KeyEvent.VK_LEFT :
                keyLeft = false;
                break;
            case KeyEvent.VK_RIGHT :
                keyRight = false;
                break;
            case KeyEvent.VK_UP :
                keyUp = false;
                break;
            case KeyEvent.VK_DOWN :
                keyDown = false;
                break;
        }
    }

    public void keyProcess(){
//여기서는 단순 케릭터가 이동하는 좌표 말고도
//케릭터의 움직임 여부및 방향을 체크 합니다.
        playerMove = false;

        if ( keyUp ){
            playerMove = true;
            y -= 8;
            moveStatus = 0;
        }

        if ( keyDown){
            y += 8;
            moveStatus = 2;
            playerMove = true;
        }

        if ( keyLeft){
            x -= 8;
            moveStatus = 3;
            playerMove = true;
        }

        if ( keyRight){
            x += 8;
            moveStatus = 1;
            playerMove = true;
        }
    }


    public void DrawImg(){
        gc.setFont(new Font("Default", Font.BOLD, 20));
        gc.drawString(Integer.toString(cnt), 50, 50);
        gc.drawString(Integer.toString((playerMove)?1:0),200, 50);
//위는 단순히 무한루프 적용여부와 케릭터 방향 체크를 위해
//눈으로 보면서 테스트할 용도로 쓰이는 텍스트 표출입니다.

        MoveImage(img, x, y, 100, 150);
//케릭터를 걸어가게 만들기 위해 추가로 만든 메소드 입니다.
    }

    public void MoveImage(Image img, int x, int y, int width,
                          int height){
//케릭터 이미지, 케릭터 위치, 케릭터 크기를 받습니다.
//받은 값을 이용해서 위의 이미지칩셋에서 케릭터를 잘라내
//표출하도록 계산하는 메소드 입니다.

        gc.setClip(x  , y, width, height);
//현재 좌표에서 케릭터의 크기 만큼 이미지를 잘라 그립니다.

        if( playerMove ){ // 케릭터의 움직임 여부를 판단합니다.
            if ( cnt / 10 % 4 == 0 ){
                gc.drawImage(img,
                    x - ( width * 0 ), y - ( height * moveStatus ), this);

            }else  if(cnt/10%4 == 1){
                gc.drawImage(img,
                    x - ( width * 1 ), y - ( height * moveStatus ), this);

            }else  if(cnt/10%4 == 2){
                gc.drawImage(img,
                    x - ( width * 2 ), y - ( height * moveStatus ), this);

            }else  if(cnt/10%4 == 3){
                gc.drawImage(img,
                    x - ( width * 1 ), y - ( height * moveStatus ), this);
            }
//케릭터의 방향에 따라 걸어가는 모션을 취하는
//케릭터 이미지를 시간차를 이용해 순차적으로 그립니다.

        }else { gc.drawImage(img, x - ( width * 1 ),
                y - ( height * moveStatus ), this);
//케릭터가 움직이지 않으면 정지한 케릭터를 그립니다.

        }
    }
    public void keyTyped(KeyEvent e) {}

}