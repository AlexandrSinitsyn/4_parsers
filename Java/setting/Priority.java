package expression.generic.setting;

public interface Priority {

    class GlobalPriorities {
        public static int BASE   =   0;
        public static int MIN    =   100;
        public static int OR     =   200;
        public static int XOR    =   300;
        public static int AND    =   400;
        public static int LOW    =   500;
        public static int HIGH   =   600;
        public static int UNARY  =   700;
    }


    int getPriority();
}
