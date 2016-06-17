{
$:{(S names, Bool isFwd, S mutK,S lentK,S readK,S immK)
    method
    This fwd()
        This(names:this.names(), isFwd:Bool.true(),
            mutK:this.mutK(),lentK:this.lentK(),readK:this.readK(),immK:this.immK() )
    method
    Library<< (Library that)
    exception
    MetaGuard.MethodUnavailable//if names are wrong
    //MetaGuard.NestedClassUnavailable//no since we use Path"This"
    {
        return SafeOperators.addKs(that,
            path:Path"This", fields:this.names(),//add parameter for path?
            mutK:this.mutK(), lentK:this.lentK(), readK:this.readK(), immK:this.immK(),
            isFwd:this.isFwd())
        catch error Library err (
          //MetaGuard.NestedClassUnavailable(lift:err)
          MetaGuard.MethodUnavailable(lift:err)
          error err)
        }
    }
class method
This.$ #from(S.SBuilder builder)
    This.$(names:S.#from(builder:builder),  isFwd:Bool.false(),
        mutK:S"#apply", lentK:S"lentK", readK: S"readK",immK:S"immK")
class method
mut S.SBuilder #builder()
    S.SBuilder.empty()
class method
Library<< (Library that){
    //compute names
    i=Introspection(lib:that)
    names=S""[with m in i.methods().vals() (
        sel=m.selector()
        isAbs=m.methodKind().equals(\class._AbstractMethod())
        isVis=!m.isPrivate()
        isNoArg=sel.names().isEmpty()
        if isAbs & isVis & isNoArg ( use[sel.nameAsField() separedBy:S","] )
        )]""
    //just submit mutK...immK, if already there, they are not produced by the operator.
    return This.$(names:names,  isFwd:Bool.false(),
        mutK:S"#apply", lentK:S"lentK", readK: S"readK",immK:S"immK")
        <<that
    error on MetaGuard.MethodUnavailable Assert.CodeNotReachable""
  }
}