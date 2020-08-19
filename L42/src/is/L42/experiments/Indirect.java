package is.L42.experiments;
import is.L42.platformSpecific.javaEvents.*;
import safeNativeCode.slave.Slave;
import safeNativeCode.slave.host.ProcessSlave;
public class Indirect {
  public static void main(String[]a) throws Throwable{
    String javaCode="""
      package miniGui;
      import javax.swing.JButton;
      import javax.swing.JFrame;
      import javax.swing.SwingUtilities;
      public class MiniGui2 {
        public static void main(String[]a) {new MiniGui2();}
        public MiniGui2(){SwingUtilities.invokeLater(this::make);}
        void make(){
          var frame = new JFrame("MiniGui");
          frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          frame.setSize(300,300);
          var button = new JButton("Press");
          button.addActionListener(e->{
            System.out.println("pressed");
            });
          frame.getContentPane().add(button);
          frame.setVisible(true);
          }
        }
      """;
    String[] args = new String[]{"--enable-preview"};
    Slave s=new ProcessSlave(0, args, ClassLoader.getPlatformClassLoader());
    s.addClassLoader(new Object(){}.getClass().getEnclosingClass().getClassLoader());
    //This throws a strange exception
    s.call(()->LoadJavaCode.loadJavaCode("miniGui.MiniGui2",javaCode));
    //But this works
    //LoadJavaCode.loadJavaCode("miniGui.MiniGui2",javaCode);
    }
  }