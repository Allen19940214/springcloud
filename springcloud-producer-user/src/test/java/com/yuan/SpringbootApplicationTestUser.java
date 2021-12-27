package com.yuan;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@SpringBootTest
//@RunWith(SpringRunner.class)
public class SpringbootApplicationTestUser {
    @Test
    public void test1(){
        Node jack = new Node("jack");
        Node marry = new Node("marry");
        Node tom = new Node("tom");
        jack.next=marry;
        marry.next=tom;
        tom.pre=marry;
        marry.pre=jack;
        Node first=jack;
        Node last=tom;
        while (first!=null){
            System.out.println(first);
            first=first.next;
        }
    }
}
class Node{
    public Object item;
    public Node next;
    public Node pre;

    public Node(Object item) {
        this.item = item;
    }

    @Override
    public String toString() {
        return "Node name="+item;
    }
}