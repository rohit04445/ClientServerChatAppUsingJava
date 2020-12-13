import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;

import java.util.Calendar;
import java.text.SimpleDateFormat;

public class Client implements ActionListener,KeyListener{

    JPanel p1;
    JTextField t1;
    JButton b1;
    static JPanel a1;
    static JFrame f1 = new JFrame();

    static Box vertical = Box.createVerticalBox();


    static Socket s;
    static DataInputStream din;
    static DataOutputStream dout;

    Boolean typing;

    Client(){
        f1.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        p1 = new JPanel();
        p1.setLayout(null);
        p1.setBackground(new Color(7, 94, 84));
        p1.setBounds(0, 0, 450, 70);
        f1.add(p1);

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("3.png"));
        Image i2 = i1.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel l1 = new JLabel(i3);
        l1.setBounds(5, 17, 30, 30);
        p1.add(l1);

        l1.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent ae){
                System.exit(0);
            }
        });




        JLabel l3 = new JLabel("Client to Server");
        l3.setFont(new Font("SAN_SERIF", Font.BOLD, 18));
        l3.setForeground(Color.WHITE);
        l3.setBounds(110, 15, 200, 18);
        p1.add(l3);


        JLabel l4 = new JLabel("Active Now");
        l4.setFont(new Font("SAN_SERIF", Font.PLAIN, 14));
        l4.setForeground(Color.WHITE);
        l4.setBounds(110, 35, 100, 20);
        p1.add(l4);

        Timer t = new Timer(1, ae -> {
            if(!typing){
                l4.setText("Active Now");
            }
        });

        t.setInitialDelay(2000);

        a1 = new JPanel();
        a1.setBounds(5, 75, 440, 570);
        a1.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        JScrollPane s=new JScrollPane(a1);
        s.setBounds(5,75,440,570);
        s.setBorder(BorderFactory.createEmptyBorder());
        f1.add(s);

        t1 = new JTextField();
        t1.setBounds(5, 655, 310, 40);
        t1.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        f1.add(t1);

        t1.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent ke){
                l4.setText("typing...");
                t.stop();
                typing = true;

            }

            public void keyReleased(KeyEvent ke){
                typing = false;
                if(!t.isRunning()){
                    t.start();
                }
                if(ke.getKeyCode()==10)
                {
                    try{
                        String out = t1.getText();

                        JPanel p2 = formatLabel(out);

                        a1.setLayout(new BorderLayout());

                        JPanel right = new JPanel(new BorderLayout());
                        right.add(p2, BorderLayout.LINE_END);
                        vertical.add(right);
                        vertical.add(Box.createVerticalStrut(15));

                        a1.add(vertical, BorderLayout.PAGE_START);

                        //a1.add(p2);
                        dout.writeUTF(out);
                        t1.setText("");
                    }catch(Exception e){
                        System.out.println(e);
                    }
                }

            }
        });

        b1 = new JButton("Send");
        b1.setBounds(320, 655, 123, 40);
        b1.setBackground(new Color(7, 94, 84));
        b1.setForeground(Color.WHITE);
        b1.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        b1.addActionListener(this);
        f1.add(b1);


        f1.getContentPane().setBackground(Color.WHITE);
        f1.setLayout(null);
        f1.addKeyListener(this);
        f1.setFocusable(true);
        f1.setSize(450, 700);
        f1.setLocation(600, 0);
        f1.setUndecorated(true);
        f1.setVisible(true);

    }

    public void actionPerformed(ActionEvent ae){

        try{
            String out = t1.getText();

            JPanel p2 = formatLabel(out);

            a1.setLayout(new BorderLayout());

            JPanel right = new JPanel(new BorderLayout());
            right.add(p2, BorderLayout.LINE_END);
            vertical.add(right);
            vertical.add(Box.createVerticalStrut(15));

            a1.add(vertical, BorderLayout.PAGE_START);

            //a1.add(p2);
            dout.writeUTF(out);
            t1.setText("");
        }catch(Exception e){
            System.out.println(e);
        }
    }

    public static JPanel formatLabel(String out){
        JPanel p3 = new JPanel();
        p3.setLayout(new BoxLayout(p3, BoxLayout.Y_AXIS));

        JLabel l1 = new JLabel("<html><p style = \"width : 150px\">"+out+"</p></html>");
        l1.setFont(new Font("Tahoma", Font.PLAIN, 16));
        l1.setBackground(new Color(37, 211, 102));
        l1.setOpaque(true);
        l1.setBorder(new EmptyBorder(15,15,15,50));

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        JLabel l2 = new JLabel();
        l2.setText(sdf.format(cal.getTime()));

        p3.add(l1);
        p3.add(l2);
        return p3;
    }

    public static void main(String[] args){
        new Client().f1.setVisible(true);

        try{

            s = new Socket("127.0.0.1", 6001);
            din  = new DataInputStream(s.getInputStream());
            dout = new DataOutputStream(s.getOutputStream());

            String msginput;

            while(true){
                a1.setLayout(new BorderLayout());
                msginput = din.readUTF();
                JPanel p2 = formatLabel(msginput);
                JPanel left = new JPanel(new BorderLayout());
                left.add(p2, BorderLayout.LINE_START);

                vertical.add(left);
                vertical.add(Box.createVerticalStrut(15));
                a1.add(vertical, BorderLayout.PAGE_START);
                f1.validate();
            }

        }catch(Exception ignored){}
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
        JPanel p2 = formatLabel(""+keyEvent.getKeyChar());

        a1.setLayout(new BorderLayout());

        JPanel right = new JPanel(new BorderLayout());
        right.add(p2, BorderLayout.LINE_END);
        vertical.add(right);
        vertical.add(Box.createVerticalStrut(15));

        a1.add(vertical, BorderLayout.PAGE_START);
        System.out.println(keyEvent.getKeyChar());

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
      System.out.println(keyEvent.getKeyChar());
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        System.out.println(keyEvent.getKeyChar());
    }
}