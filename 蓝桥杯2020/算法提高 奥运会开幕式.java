package cn.gk.test02;

import java.util.Scanner;

/**
 * 试题 算法提高 奥运会开幕式
 * 	问题描述
　　学校给高一（三）班分配了一个名额，去参加奥运会的开幕式。每个人都争着要去，可是名额只有一个，怎么办？班长想出了一个办法，
	让班上的所有同学（共有n个同学）围成一圈，按照顺时针方向进行编号。然后随便选定一个数m，并且从1号同学开始按照顺时针方向依次报数，
	1, 2, …, m，凡报到m的同学，都要主动退出圈子。然后不停地按顺时针方向逐一让报出m者出圈，最后剩下的那个人就是去参加开幕式的人。
　　要求：用环形链表的方法来求解。所谓环形链表，即对于链表尾结点，其next指针又指向了链表的首结点。基本思路是先创建一个环形链表，
		模拟众同学围成一圈的情形。然后进入循环淘汰环节，模拟从1到m报数，每次让一位同学（结点）退出圈子。
　　输入格式：输入只有一行，包括两个整数n和m，其中n和 m的含义如上所述。
　　输出格式：输出只有一个整数，即参加开幕式的那个人的编号。
	输入输出样例
	样例输入
	8 3
	样例输出
	7
 * 
 * @author 郭凯
 *
 */
public class Main03 
{

	public static void main(String[] args) 
	{
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		int m = sc.nextInt();
		sc.close();
		
		Student head = new Student(0);
		//使用指针p保存头指针
		Student p = head;
		
		for(int i=1;i<=n;i++)
		{
			//每次创建一个对象
			Student cur = new Student(i);
			//用p指针做操作设置链表的下一个对象
			p.setNext(cur);
			//调整p的位置
			p = p.getNext();
		}
		//结束for循环时还是单向链表，现在调整成环形
		head = head.getNext();//让head指向1
		p.setNext(head);//设置最后一个的下一个是1
		
		int cnt = n;//剩余人数
		int num = 1;//从1开始报数
		
		//用cur指针等于head指针，开始进行报数淘汰操作
		Student cur = head;
		while(cnt>1)
		{
			if(cur.isFlag()==true){//该学生已经被淘汰了
				cur = cur.getNext();
				continue;
			}
			if(num == m)
			{
				cur.setFlag(true);//把当前学生淘汰
				cnt--;
				num = 1;
				cur = cur.getNext();
				continue;
			}
			num++;
			cur = cur.getNext();
		}
		
		for(int i=1;i<=n;i++)
		{
			if(head.isFlag()==false) System.out.println(head.getNumber());
			head = head.getNext();
		}
	}
}
class Student{
	private Integer number;
	private boolean flag = false;//标记是否被删除
	private Student next;//下一个学生
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public Student getNext() {
		return next;
	}
	public void setNext(Student next) {
		this.next = next;
	}
	public Student(Integer number) {
		super();
		this.number = number;
	}
}