package com.taboola;

import com.taboola.question1.SolutionOne;
import com.taboola.question2.SolutionTwo;
import lombok.val;



public class Main {
    public static void main(String[] args) {
        if(args.length>0 && "1".equals(args[0])) {
            val s1 = new SolutionOne();
            s1.run();
        }else {
            val s2 = new SolutionTwo();
            s2.run();
        }
    }


}
