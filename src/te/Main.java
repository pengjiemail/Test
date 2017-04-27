package te;

import java.text.DecimalFormat;
import java.util.Scanner;
import java.util.regex.Pattern;



public class Main {
  public static void main(String[] args) {
    Scanner sc=new Scanner(System.in);
    Pattern p=Pattern.compile("[\\d]+");
    if(sc.hasNext(p)){
      int count=sc.nextInt();
      
      for(int i=0;i<count;i++){
        int si=0,sj=0,snl=0,swy=0,ssm=0,wi=0,wj=0,wnl=0,wwy=0,wsm=0,ei=0,ej=0,enl=0,ewy=0,esm=0,sgj=0,wgj=0,egj=0;
        int num=sc.nextInt();
        while(sc.hasNext()){
          String menpai=sc.next();
          if(!menpai.equals("0")){
            if(menpai.equals("S")){
              si=sc.nextInt();
              sj=sc.nextInt();
              snl=sc.nextInt();
              swy=sc.nextInt();
              ssm=sc.nextInt();
              sgj=Integer.valueOf(new DecimalFormat("0").format(Math.floor((0.5*snl+0.5*swy)*(ssm+10)/100)));
            }
            if(menpai.equals("W")){
              wi=sc.nextInt();
              wj=sc.nextInt();
              wnl=sc.nextInt();
              wwy=sc.nextInt();
              wsm=sc.nextInt();
              wgj=Integer.valueOf(new DecimalFormat("0").format(Math.floor((0.8*wnl+0.2*wwy)*(wsm+10)/100)));
            }
            if(menpai.equals("E")){
              ei=sc.nextInt();
              ej=sc.nextInt();
              enl=sc.nextInt();
              ewy=sc.nextInt();
              esm=sc.nextInt();
              egj=Integer.valueOf(new DecimalFormat("0").format(Math.floor((0.2*enl+0.8*ewy)*(esm+10)/100)));
            }
          }else{
            break;
          }
        }
        int sflag=0,wflag=0,eflag=0;
        for(int j=0;j<num;j++){
          if(si!=0&&wi!=0&&(si==wi&&sj==wj)&&!(ei==si&&ej==sj)){
            ssm=ssm-wgj;
            wsm=wsm-sgj;
            sgj=Integer.valueOf(new DecimalFormat("0").format(Math.floor((0.5*snl+0.5*swy)*(ssm+10)/100)));
            wgj=Integer.valueOf(new DecimalFormat("0").format(Math.floor((0.8*wnl+0.2*wwy)*(wsm+10)/100)));
            if(ssm<=0){
              si=0;
              sj=0;
              snl=0;
              swy=0;
              ssm=0;
              sgj=0;
            }
            if(wsm<=0){
              wi=0;
              wj=0;
              wnl=0;
              wwy=0;
              wsm=0;
              wgj=0;
            }
          }
          if(si!=0&&ei!=0&&(si==ei&&sj==ej)&&!(wi==si&&wj==sj)){
            ssm=ssm-egj;
            esm=esm-sgj;
            sgj=Integer.valueOf(new DecimalFormat("0").format(Math.floor((0.5*snl+0.5*swy)*(ssm+10)/100)));
            egj=Integer.valueOf(new DecimalFormat("0").format(Math.floor((0.2*enl+0.8*ewy)*(esm+10)/100)));
            if(ssm<=0){
              si=0;
              sj=0;
              snl=0;
              swy=0;
              ssm=0;
              sgj=0;
            }
            if(esm<=0){
              ei=0;
              ej=0;
              enl=0;
              ewy=0;
              esm=0;
              egj=0;
            }
          }
          if(wi!=0&&ei!=0&&(wi==ei&&wj==ej)&&!(si==wi&&sj==wj)){
            wsm=wsm-egj;
            esm=esm-wgj;
            wgj=Integer.valueOf(new DecimalFormat("0").format(Math.floor((0.8*wnl+0.2*wwy)*(wsm+10)/100)));
            egj=Integer.valueOf(new DecimalFormat("0").format(Math.floor((0.2*enl+0.8*ewy)*(esm+10)/100)));
            if(wsm<=0){
              wi=0;
              wj=0;
              wnl=0;
              wwy=0;
              wsm=0;
              wgj=0;
            }
            if(esm<=0){
              ei=0;
              ej=0;
              enl=0;
              ewy=0;
              esm=0;
              egj=0;
            }
          }
          if(si!=0){
            if(si>=1&&si<12&&sflag==0){
              si++;
            }else{
              si--;
              if(si==1){
                sflag=0;
              }else{
                sflag=-1;
              }
            }
          }
          if(wj!=0){
            if(wj>=1&&wj<12&&wflag==0){
              wj++;
            }else{
              wj--;
              if(wj==1){
                wflag=0;
              }else{
                wflag=-1;
              }
            }
          }
          if(ei!=0){
            if(!((ei==1&&ej==12)||(ei==12&&ej==1))){
              if(ei>=1&&ei<12&&eflag==0){
                ei++;
                ej++;
              }else{
                ei--;
                ej--;
                if(ei==1){
                  eflag=0;
                }else{
                  eflag=-1;
                }
              }
            }
          }
        }
        if(si!=0){
          System.out.println("1 "+ssm);
        }else{
          System.out.println("0 0");
        }
        if(wi!=0){
          System.out.println("1 "+wsm);
        }else{
          System.out.println("0 0");
        }
        if(ei!=0){
          System.out.println("1 "+esm);
        }else{
          System.out.println("0 0");
        }
        System.out.println("***");
      }
    }
  }
}
