package is.L42.top;

/*
 
   public static Cache loadCache(Path path){
    if(path.endsWith("localhost")){//TODO: good just for testing
      return new Cache(new ArrayList<>(), new ArrayList<>(), null);
      } 
    try(
      var file=new FileInputStream(path.resolve("cache.L42Bytes").toFile()); 
      var out=new ObjectInputStream(file);
      ){return (Cache)out.readObject();}
    catch(FileNotFoundException e){return new Cache(new ArrayList<>(),new ArrayList<>(),null);}
    catch(ClassNotFoundException e){throw unreachable();}
    catch(IOException e){throw new Error(e);}
    }
  public static void saveCache(Path path,Cache cache){
    if(path.endsWith("localhost")){return;}//TODO: good just for testing
    try(
      var file=new FileOutputStream(path.resolve("cache.L42Bytes").toFile()); 
      var out=new ObjectOutputStream(file);
      ){out.writeObject(cache);}
    catch (FileNotFoundException e) {throw new Error(e);}
    catch (IOException e) {
      e.printStackTrace();
      throw todo();
      }
    }
  }
 */