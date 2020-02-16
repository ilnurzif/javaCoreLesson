enum MsgType {PRIVATE, PUBLIC};

// входная строка будет иметь вид либо: "сообщение" когда оно адресовано всем
// либо "сообщение|PRIVATE|userName" когда оно приватное
// можно ограничиться только видом "сообщение|userName" тогда код не застрахован от испольсования символа "|"

public class Msg {
    private String msg;
    private MsgType msgType = MsgType.PUBLIC;
    private String userName;

    public Msg(String msg) {
        this.msg = msg;
        this.msgType = MsgType.PUBLIC;
        msgParse(msg);
    }

    private void msgParse(String msg) {
        String[] subStr;
        subStr = msg.split("!");
        if ((subStr.length == 3) && (subStr[1].equals("PRIVATE"))) {
            {
                this.msg = subStr[0];
                this.msgType = MsgType.PRIVATE;
                this.userName = subStr[2];
            }
        }
    }

    public MsgType getMsgType() {
        return msgType;
    }

    public String getMsg() {
        return msg;
    }

    public String getUserName() {
        return userName;
    }
}
