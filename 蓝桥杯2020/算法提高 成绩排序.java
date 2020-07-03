package cn.test2.gk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

/**
 * 算法提高 成绩排序
 *   问题描述
	　　给出n个学生的成绩，将这些学生按成绩排序，
	　　排序规则，优先考虑数学成绩，高的在前；数学相同，英语高的在前；数学英语都相同，语文高的在前；三门都相同，学号小的在前
	输入格式
	　　第一行一个正整数n，表示学生人数
	　　接下来n行每行3个0~100的整数，第i行表示学号为i的学生的数学、英语、语文成绩
	输出格式
	　　输出n行，每行表示一个学生的数学成绩、英语成绩、语文成绩、学号
	　　按排序后的顺序输出
	样例输入
		2
		1 2 3
		2 3 4
	样例输出
		2 3 4 2
		1 2 3 1
	数据规模和约定
	　　	n≤100
 * @author 郭凯
 *
 */
public class Main07 
{

	public static void main(String[] args) 
	{
		Scanner sc = new Scanner(System.in);
		
		int n = sc.nextInt();
		
		List<Student> list = new ArrayList<Student>();
		int i=0;
		while((n--)>0){
			int math = sc.nextInt();
			int english = sc.nextInt();
			int chinese = sc.nextInt();
			Student stu = new Student();
			stu.setMath(math);
			stu.setEnglish(english);
			stu.setChinese(chinese);
			stu.setNumber(++i);
			list.add(stu);
		}
		Collections.sort(list, new MyCmp());
		for(int j=0;j<list.size();j++)
		{
			Student stu = list.get(j);
			System.out.println(stu.getMath()+" "+stu.getEnglish()+" "+stu.getChinese()+" "+stu.getNumber());
		}
		
		sc.close();
	}

}

class Student{
	private int number;
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	private int math;
	private int english;
	private int chinese;
	
	public int getMath() {
		return math;
	}
	public void setMath(int math) {
		this.math = math;
	}
	public int getEnglish() {
		return english;
	}
	public void setEnglish(int english) {
		this.english = english;
	}
	public int getChinese() {
		return chinese;
	}
	public void setChinese(int chinese) {
		this.chinese = chinese;
	}
}

class MyCmp implements Comparator<Student>{

	@Override
	public int compare(Student stu1, Student stu2) {
		
		if(stu1.getMath()>stu2.getMath()){
			return -1;
		}else if(stu1.getMath()<stu2.getMath()){
			return 1;
		}else{

			if(stu1.getEnglish()>stu2.getEnglish()){
				return -1;
			}else if(stu1.getEnglish()<stu2.getEnglish()){
				return 1;
			}else{

				if(stu1.getChinese()>stu2.getChinese()){
					return -1;
				}else if(stu1.getChinese()<stu2.getChinese()){
					return 1;
				}else{
					if(stu1.getChinese()>stu2.getChinese()){
						return 1;
					}else if(stu1.getChinese()<stu2.getChinese()){
						return -1;
					}
				}
			}
		}
		return 0;
	}
	
}