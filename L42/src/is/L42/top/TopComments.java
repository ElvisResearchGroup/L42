package is.L42.top;
/*
 Deploy time without caching: 127 seconds
 
albero a due livelli, quindi ci sono due tipi di I,M,O,D?

-{reuse[url] ncs}
  LI: the content of reuse  
  LM: the full method bodies
  process reuse, process nesteds: e nodes
  use nesteds to process meth bodies
  return coreL 

-e[Ls]
  EI: the coreExpression
  EM:the halfExpression?
  process eFull->eHalf.
  then process eHalf->eCore; this calls L nodes inside
  use eCode (with coreLs inside) to compute coreL
  return coreL
DL: 
  can give I=reuse, DEs, M=halfMeths
DE:
  can give fullE, DLs, M=halfE
Modes:
  L1:getReuse(LI) -> L3
  L2:getMethBodies(LM) -> E3
  L3:dispatch(EI/LM) -> E1/L2
  E1:getFullE(EI) -> E3
  E2:executeE(EM) -> L3
  E3:dispatch(LI/EM) -> L1/E2

D::= D[I;Ds;M]
R::= R[g;O]|Err
C::= C[D;gr;Cs;R]

example
   D[fullL]
   R[_;coreL]
   C[fullL;_;[];_;coreL]
   g|-C[fullL;_;[];_;coreL] == reuseUrl : g //g is returned untouched
     defined as: fullL.reuseUrl==reuseUrl, and either 
       - no #$ or
       - follow(url)== coreL\dom(fullL) 

   D[fullNC]
   R[_;coreNC]
   C[fullNC;g1;[];_;coreNC]
   g|-C[fullNC;g1;[];_;coreNC] == halfNC : g0
     defined as: g1.lastHalfNC == halfNC, and either 
       - no #$ or
       - execute(halfNC) == coreNC.coreL 
     g0 is like g, but when g0(halfNC,[..]) is called, it remembers the execute computation 
*/
/*
the 4 actions:
topOpen
topClose
topNCiOpen
topNCiClose (do the second part of infer)

infer need to be split in 2 parts:
--halfE with Full.L->halfE with only Core.L
--infer with a null Top

LI and EI:
LI have HalfE and index: the index is the Full.L in HalfE that we reached
EI have Full.L and index: the index is the number of the nested class that we reached

*/