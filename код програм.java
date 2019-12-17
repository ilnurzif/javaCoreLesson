//1 задача
class Utility {
    public static void msgPrint(String msg) {
        System.out.println(msg);
    }
}

public class Man {
    private final String runMsg="Человек бежит";
    private final String jumpMsg="Человек прыгает";
    public void Run() {
	  Utility.msgPrint(runMsg);
    }
    public void Jump() {
      Utility.msgPrint(jumpMsg);
    }

    public static void main(String[] args) {
     Man man=new Man();
     man.Jump();
    }
}

public class Cat {
    private final String runMsg="Кот бежит";
    private final String jumpMsg="Кот прыгает";
    public void Run() {
      Utility.msgPrint(runMsg);
    }
    public void Jump() {
      Utility.msgPrint(jumpMsg);
    }

    public static void main(String[] args) {
     Cat cat=new Cat();
     cat.Jump();
    }
}

public class Robot {
    private final String runMsg="Робот бежит";
    private final String jumpMsg="Робот прыгает";
    public void Run() {
      Utility.msgPrint(runMsg);
    }
    public void Jump() {
      Utility.msgPrint(jumpMsg);
    }

    public static void main(String[] args) {
      Robot robot=new Robot();
      robot.Jump();
    }
}

//2 задача
class Member {
  private final String okRun="успешно пробежал";
  private final String noRun="не пробежал";
  private final String okJump="перепрыгнул";
  private final String noJump="не перепрыгнул";

  private float distanceMax;
  private float jumpMax;

    public Member(float distanceMax, float jumpMax) {
    this.distanceMax=distanceMax;
    this.jumpMax=jumpMax;
  }

  public void run(float distance) {
    if (distance-distanceMax<=0)
     Utility.msgPrint(okRun);
    else
     Utility.msgPrint(noRun);
   }

    public void jump(float height) {
     if (height-jumpMax<=0)
      Utility.msgPrint(okJump);
     else
      Utility.msgPrint(noJump);
    }
}

abstract class Barrier {
 protected float maxval;
 public Barrier(float maxval) {
   this.maxval=maxval;
 }

public abstract void doIt(Member member);
}

class RunningTrack extends Barrier {
    public RunningTrack(float maxval) {
        super(maxval);
    }

    @Override
  public void doIt(Member member) {
      member.run(maxval);
  }
}

class Wall extends Barrier {
    public Wall(float maxval) {
        super(maxval);
    }

    @Override
    public void doIt(Member member) {
        member.jump(maxval);
    }
}

public class MainClass {
    public static void main(String[] args) {
        Member member1=new Member(10,2);
        Member member2=new Member(5.2f,   1.1f);

        Barrier runningTrack=new RunningTrack(6);
        Barrier wall=new Wall(1.4f);

        runningTrack.doIt(member1);
        runningTrack.doIt(member2);
        wall.doIt(member1);
        wall.doIt(member2);
    }
}
// 3 задача
public class MainClass {
    private static final String memberName="Участник № ";
    public static void main(String[] args) {
        Member[] arrMember={new Member(10,2),
                            new Member(5.2f,   1.1f),
                            new Member(3,   1.5f),
                            new Member(6.3f,   2.2f)     
                          };

        Barrier[] arrBarrier={
                new RunningTrack(6),
                new Wall(1.4f)
           };

        for (int j = 0; j < arrMember.length; j++)
            for (int i = 0; i < arrBarrier.length; i++) {
                System.out.print(memberName+(j+1)+" ");
                arrBarrier[i].doIt(arrMember[j]);
            }

    }
}

// 4 задача
class Utility {
    public static void msgPrint(String msg) {
        System.out.println(msg);
    }
}

class Member {
  private final String okRun="успешно пробежал";
  private final String noRun="не пробежал";
  private final String okJump="перепрыгнул";
  private final String noJump="не перепрыгнул";

  private boolean barrierStatus=true;

  private float distanceMax;
  private float jumpMax;

    public Member(float distanceMax, float jumpMax) {
    this.distanceMax=distanceMax;
    this.jumpMax=jumpMax;
  }

  public void run(float distance) {
    if (distance-distanceMax<=0) {
     Utility.msgPrint(okRun);
     barrierStatus=true;
    }
    else {
     Utility.msgPrint(noRun);
     barrierStatus=false;
    }
   }

   public boolean getStatus() {
     return barrierStatus;
   }

    public void jump(float height) {
     if (height-jumpMax<=0)
     {  Utility.msgPrint(okJump);
         barrierStatus=true;
     }
     else {
       Utility.msgPrint(noJump);
       barrierStatus=true;
     }
    }
}

abstract class Barrier {
 protected float maxval;
 public Barrier(float maxval) {
   this.maxval=maxval;
 }

public abstract void doIt(Member member);
}

class RunningTrack extends Barrier {
    public RunningTrack(float maxval) {
        super(maxval);
    }

    @Override
    public void doIt(Member member) {
        member.run(maxval);
    }
}

class Wall extends Barrier {
    public Wall(float maxval) {
        super(maxval);
    }

    @Override
    public void doIt(Member member) {
      member.jump(maxval);
    }
}

public class MainClass {
    private static final String memberName = "Участник № ";

    public static void main(String[] args) {
        Member[] arrMember = {new Member(10, 2),
                new Member(5.2f, 1.1f),
                new Member(3, 1.5f),
                new Member(6.3f, 2.2f)
        };

        Barrier[] arrBarrier = {
                new RunningTrack(6),
                new Wall(1.4f)
        };

        for (int j = 0; j < arrMember.length; j++)
            {
                boolean status = true;
                for (int i = 0; i < arrBarrier.length; i++) {
                    arrBarrier[i].doIt(arrMember[j]);
                    status = arrMember[j].getStatus();
                    if (!status) break;
                }
                if (!status)
                    Utility.msgPrint(memberName + (j + 1) + " не прошел всех испытаний");
                else
                    Utility.msgPrint(memberName + (j + 1) + " прошел все испытания");
            }
        }
    }


