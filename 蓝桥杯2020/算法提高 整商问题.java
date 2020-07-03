package cn.test2.gk;

import java.util.Scanner;

/**
 * 试题 算法提高 整商问题
 *   问题描述
	　　提示用户输入被除数（dividend）和除数（divisor），若除数为0，则提示用户重新输入，直至除数非零为止。最后输出商。程序建议大家将被除数、除数和商都定义为整形。
	　　输入被除数提示语句为：Please enter the dividend:
	　　输入除数提示语句为：Please enter the divisor:
	　　提示除数为0需要重新输入的语句为： Error: divisor can not be zero! Please enter a new divisor:
	　　建议：大家直接复制上述语句，以免出现不必要的错误。
	输入格式
	　　被除数 除数
	　　注：若除数为零，则需要连续输入除数直至其非零为止。
	输出格式
	　　提示性语句 商
	　　注意每个提示性语句输出后需要换行，具体参考下面的样例输出。
	样例输入
	一个满足题目要求的输入范例。
	例1：
		10 2
	例2:
		20 0 0 4
	样例输出
		与上面的样例输入对应的输出。
		例1：
		Please enter the dividend:
		Please enter the divisor:
		5
	例2:
		Please enter the dividend:
		Please enter the divisor:
		Error: divisor can not be zero! Please enter a new divisor:
		Error: divisor can not be zero! Please enter a new divisor:
		5
	数据规模和约定
	　　无
 * @author 郭凯
 *
 */
public class Main03 {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		
		System.out.println("Please enter the dividend:");
		int num1 = sc.nextInt();
		System.out.println("Please enter the divisor:");
		int num2 = sc.nextInt();
		while(num2==0){
			System.out.println("Error: divisor can not be zero! Please enter a new divisor:");
			num2 = sc.nextInt();
		}
		System.out.println(num1/num2);
		sc.close();

	}

}
