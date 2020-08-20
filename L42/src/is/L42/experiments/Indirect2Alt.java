package is.L42.experiments;
import safeNativeCode.slave.Slave;
import safeNativeCode.slave.host.ProcessSlave;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.beans.PropertyChangeListener;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.MetalTheme;
class FixDefaults extends UIDefaults{
  UIDefaults inner;
  FixDefaults(UIDefaults inner){this.inner=inner;}
  public void addPropertyChangeListener(PropertyChangeListener listener) {inner.addPropertyChangeListener(listener);}
  public void addResourceBundle(String bundleName) {inner.addResourceBundle(bundleName);}
  //invisible? protected void firePropertyChange(String propertyName, Object oldValue, Object newValue){inner.firePropertyChange(propertyName,oldValue,newValue);}
  public Object get(Object key) {return inner.get(key);}
  public Object get(Object key, Locale l) {return inner.get(key,l);}
  public boolean getBoolean(Object key) {return inner.getBoolean(key);}
  public boolean getBoolean(Object key, Locale l) {return inner.getBoolean(key,l);}
  public Border getBorder(Object key){return inner.getBorder(key);}
  public Border getBorder(Object key, Locale l){return inner.getBorder(key,l);}
  public Color getColor(Object key) {return inner.getColor(key);}
  public Color getColor(Object key, Locale l) {return inner.getColor(key,l);}
  public Locale getDefaultLocale() {return inner.getDefaultLocale();}
  public Dimension getDimension(Object key) {return inner.getDimension(key);}
  public Dimension getDimension(Object key, Locale l) {return inner.getDimension(key,l);}
  public Font getFont(Object key) {return inner.getFont(key);}
  public Font getFont(Object key, Locale l) {return inner.getFont(key,l);}
  public Icon getIcon(Object key) {return inner.getIcon(key);}
  public Icon getIcon(Object key, Locale l) {return inner.getIcon(key,l);}
  public Insets getInsets(Object key) {return inner.getInsets(key);}
  public Insets getInsets(Object key, Locale l) {return inner.getInsets(key,l);}
  public int getInt(Object key) {return inner.getInt(key);}
  public int getInt(Object key, Locale l) {return inner.getInt(key,l);}
  public PropertyChangeListener[] getPropertyChangeListeners() {return inner.getPropertyChangeListeners();}
  public String getString(Object key) {return inner.getString(key);}
  public String getString(Object key, Locale l) {return inner.getString(key,l);}
  //public ComponentUI getUI(JComponent target) {return inner.getUI(target);}//TODO:
  public ComponentUI getUI(JComponent target) {
      Object cl = get("ClassLoader");
      ClassLoader uiClassLoader =
        (cl != null) ? (ClassLoader)cl : target.getClass().getClassLoader();
        Class<? extends ComponentUI> uiClass = getUIClass(target.getUIClassID(), uiClassLoader);
        Object uiObject = null;
        if (uiClass == null) {getUIError("no ComponentUI class for: " + target);}
        else {try {
            Method m = (Method)get(uiClass);
            if (m == null) {
                m = uiClass.getMethod("createUI", new Class<?>[]{uiClassLoader.loadClass("javax.swing.JComponent")});//EDITED
                put(uiClass, m);
            }
            if (uiClass.getModule() == ComponentUI.class.getModule()) {
                // uiClass is a system LAF if it's in java.desktop module
                uiObject = m.invoke(null, new Object[]{target});
            } else {
                throw new Error();//TODO:
                //uiObject = MethodUtil.invoke(m, null, new Object[]{target});
            }
        }
        catch (NoSuchMethodException e) {
            getUIError("static createUI() method not found in " + uiClass);
        }
        catch (Exception e) {
            StringWriter w = new StringWriter();
            PrintWriter pw = new PrintWriter(w);
            e.printStackTrace(pw);
            pw.flush();
            getUIError("createUI() failed for " + target + "\n" + w);
        }
    }
    return (ComponentUI)uiObject;
}
  public Class<? extends ComponentUI> getUIClass(String uiClassID) {return inner.getUIClass(uiClassID);}
  public Class<? extends ComponentUI> getUIClass(String uiClassID, ClassLoader uiClassLoader){return inner.getUIClass(uiClassID,uiClassLoader);}
  //invisible? protected void getUIError(String msg) {inner.getUIError(msg);}
  public Object put(Object key, Object value){return inner.put(key, value);}
  public void putDefaults(Object[] keyValueList) {inner.putDefaults(keyValueList);}
  public void removePropertyChangeListener(PropertyChangeListener listener) {inner.removePropertyChangeListener(listener);}
  public void removeResourceBundle(String bundleName) {inner.removeResourceBundle(bundleName);}
  public void setDefaultLocale(Locale l) {inner.setDefaultLocale(l);}
  public void clear() {inner.clear();}
  public Object clone() {return new FixDefaults((UIDefaults)inner.clone());}
  public Object compute(Object key, BiFunction<Object,Object,?> remappingFunction) {return inner.compute(key, remappingFunction);}
  public Object computeIfAbsent(Object key, Function<Object,?> mappingFunction) {return inner.computeIfAbsent(key, mappingFunction);}
  public Object computeIfPresent(Object key, BiFunction<Object,Object,?> remappingFunction) {return inner.computeIfPresent(key, remappingFunction);}
  public boolean contains(Object value) {return inner.contains(value);}
  public boolean containsKey(Object key) {return inner.containsKey(key);}
  public boolean containsValue(Object value) {return inner.containsValue(value);}
  public Enumeration<Object> elements(){return inner.elements();}
  public Set<Map.Entry<Object,Object>>  entrySet(){return inner.entrySet();}
  public boolean equals(Object o) {return inner.equals(o);}
  public void forEach(BiConsumer<Object,Object> action) {inner.forEach(action);}
  //DUP public Object get(Object key) {return inner.get(key);}
  public Object getOrDefault(Object key,Object defaultValue) {return inner.getOrDefault(key, defaultValue);}
  public int hashCode(){return inner.hashCode();}
  public boolean isEmpty() {return inner.isEmpty();}
  public Enumeration<Object> keys(){return inner.keys();}
  public Set<Object> keySet(){return inner.keySet();}
  public Object merge(Object key,Object value, BiFunction<Object,Object,?> remappingFunction) {return inner.merge(key, value, remappingFunction);}
  //DUP public Object put(Object key, Object value) {return inner.put(key, value);}
  public void putAll(Map<?,?> t){inner.putAll(t);}//TODO: is it sufficient to use this?
  public Object putIfAbsent(Object key, Object value) {return inner.putIfAbsent(key, value);}
  //invisible? protected void rehash() {inner.rehash();}
  public Object remove(Object key) {return inner.remove(key);}
  public boolean remove(Object key, Object value) {return inner.remove(key,value);}
  public Object replace(Object key,Object value) {return inner.replace(key, value);}
  public boolean replace(Object key, Object oldValue, Object newValue){return inner.replace(key,oldValue, newValue);}
  public void replaceAll(BiFunction<Object,Object,?> function) {inner.replaceAll(function);}
  public int size() {return inner.size();}
  public String toString() {return inner.toString();}
  public Collection<Object> values(){return inner.values();}
  } 
class FixClassLoader extends MetalLookAndFeel{
  @Override
  public UIDefaults getDefaults(){
    UIDefaults table=super.getDefaults();
    return new FixDefaults(table);
    }
  @Override
  protected void initClassDefaults(UIDefaults table) {
      super.initClassDefaults(table);
      //table.put("ClassLoader", new ClassLoader() {});
      //table.put("ClassLoader", new Object().getClass().getClassLoader());
      System.out.println("CL ="+(JComponent.class.getClassLoader()==new Object().getClass().getClassLoader()));
      System.out.println("CL2 ="+(JButton.class.getClassLoader()==new Object().getClass().getClassLoader()));
      table.put("ClassLoader", UIDefaults.class.getClassLoader());
      //throw new Error();
      }
  }
class MiniGui4 {
  public MiniGui4(){SwingUtilities.invokeLater(this::make);}
  void make(){
    try {UIManager.setLookAndFeel(new FixClassLoader());}
    catch (UnsupportedLookAndFeelException e) {throw new Error(e);}
    var frame = new JFrame("MiniGui");
    System.err.println("A");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    System.err.println("B");
    frame.setSize(300,300);
    System.err.println("C");
    var button = new JButton("PressMe");
    //var button2 = new JButton("PressMeMore");
    System.err.println("D");
    button.addActionListener(e->{System.out.println("pressed");});
    frame.getContentPane().add(button);
    System.err.println("E");
    frame.setVisible(true);
    System.err.println("F");
    }
  }
public class Indirect2Alt {
  public static void main(String[]a) throws Throwable{
    String[] args = new String[]{"--enable-preview"};
    Slave s=new ProcessSlave(0, args, ClassLoader.getPlatformClassLoader());
    //s.addClassLoader(new Object(){}.getClass().getEnclosingClass().getClassLoader());
    //This throws a strange exception
    s.call(()->new MiniGui3());
    //But this works
    //new MiniGui3();
    }
  }