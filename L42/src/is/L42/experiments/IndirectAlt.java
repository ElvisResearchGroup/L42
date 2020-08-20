package is.L42.experiments;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.metal.MetalLookAndFeel;

import is.L42.platformSpecific.javaEvents.*;
import safeNativeCode.slave.Slave;
import safeNativeCode.slave.host.ProcessSlave;
public class IndirectAlt {
  public static void main(String[]a) throws Throwable{
    String javaCode="""
      package miniGui;
      import javax.swing.JButton;
      import javax.swing.JFrame;
      import javax.swing.SwingUtilities;
      import javax.swing.UIDefaults;
      import javax.swing.UIManager;
      import javax.swing.UnsupportedLookAndFeelException;
      import javax.swing.plaf.metal.MetalLookAndFeel;
      class FixClassLoader extends MetalLookAndFeel{
        @Override protected void initClassDefaults(UIDefaults table) {
           super.initClassDefaults(table);
           table.put("ClassLoader", UIDefaults.class.getClassLoader());
           }
         }
      public class MiniGui2 {
        public MiniGui2(){SwingUtilities.invokeLater(this::make);}
        void make(){
          try {UIManager.setLookAndFeel(new FixClassLoader());}
          catch (UnsupportedLookAndFeelException e) {throw new Error(e);}
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
    //s.addClassLoader(new Object(){}.getClass().getEnclosingClass().getClassLoader());
    s.addClassLoader(new Object(){}.getClass().getEnclosingClass().getClassLoader());
    //This throws a strange exception
    s.call(()->LoadJavaCode.loadJavaCode("miniGui.MiniGui2",javaCode));
    //But this works
    //LoadJavaCode.loadJavaCode("miniGui.MiniGui2",javaCode);
    }
  }