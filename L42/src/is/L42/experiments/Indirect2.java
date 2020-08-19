package is.L42.experiments;
import safeNativeCode.slave.Slave;
import safeNativeCode.slave.host.ProcessSlave;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
class MiniGui3 {
  public MiniGui3(){SwingUtilities.invokeLater(this::make);}
  void make(){
    var frame = new JFrame("MiniGui");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(300,300);
    var button = new JButton("Press");
    button.addActionListener(e->{System.out.println("pressed");});
    frame.getContentPane().add(button);
    frame.setVisible(true);
    }
  }
public class Indirect2 {
  public static void main(String[]a) throws Throwable{
    String[] args = new String[]{"--enable-preview"};
    Slave s=new ProcessSlave(0, args, ClassLoader.getPlatformClassLoader());
    s.addClassLoader(new Object(){}.getClass().getEnclosingClass().getClassLoader());
    //This throws a strange exception
    s.call(()->new MiniGui3());
    //But this works
    //new MiniGui3();
    }
  }