package cn.gk.test;

import java.util.Scanner;

/**
 * 算法提高 字符串的操作
 * 	资源限制
	时间限制：1.0s   内存限制：256.0MB
	问题描述
	　　给出一个字符串S，然后给出q条指令，分别有4种：
	
	　　1. Append str
	　　表示在S的最后追加一个字符串str。
	　　例：
	　　原字符串：ABCDE
	　　执行 Append FGHIJ 后
	　　字符串变为：ABCDEFGHIJ
	
	　　2. Insert x str
	　　表示在位置x处插入一个字符串str。（输入保证0<x<=当前字符串长度）
	　　例：
	　　原字符串：ABCGHIJ
	　　执行 Insert 4 DEF 后
	　　字符串变为：ABCDEFGHIJ
	
	　　3. Swap a b c d
	　　表示交换从第a位到第b位的字符串与从第c位到第d位的字符串。（输入保证0<a<b<c<d<=当前字符串长度）
	　　例：
	　　原字符串：ABGHIFCDEJ
	　　执行 Swap 3 5 7 9后
	　　字符串变为：ABCDEFGHIJ
	
	　　4. Reverse a b
	　　表示将从第a位到第b位的字符串反转。（输入保证0<a<b<=当前字符串长度）
	　　例：
	　　原字符串：ABGFEDCHIJ
	　　执行 Reverse 3 7 后
	　　字符串变为：ABCDEFGHIJ
	
	　　最后输出按顺序执行完指令后的字符串。
	输入格式
	　　输入第一行包含字符串S，第二行包含一个整数q，接下来q行分别为q个指令。
	输出格式
	　　输出为1行，为按顺序执行完输入指令后的字符串。
	样例输入
		My
		5
		Append Hello
		Insert 3 dlroW
		Reverse 3 7
		Swap 3 7 8 12
		Swap 1 2 3 7
	样例输出
		HelloMyWorld
	样例说明
	　　原字符串：My
	　　执行 Append Hello 后：MyHello
	　　执行 Insert 3 dlroW 后：MydlroWHello
	　　执行 Reverse 3 7 后：MyWorldHello
	　　执行 Swap 3 7 8 12 后：MyHelloWorld
	　　执行 Swap 1 2 3 7 后：HelloMyWorld
	数据规模和约定
	　　对于30%的数据，q=1；
	
	　　对于70%的数据，如有Swap指令，Swap指令中b-a=d-c；
	
	　　对于100%的数据，最终字符串长度不大于40000，1<=q<=150
 * @author 郭凯
 *
 */
public class Main {

	public static void main(String[] args)
	{
		Scanner sc = new Scanner(System.in);
		
		String str = sc.nextLine();//输入一个初始字符串
		int n = sc.nextInt();//输入命令的数量   有n个命令
		
		String ss = sc.nextLine();//将n后面输入的回车吃掉，不然程序会出现错误！
		StringBuilder sb = new StringBuilder(str);//在使用一个StringBuilder类型的对象便于之后的插入操作
		while((n--)>0)
		{
			String command = sc.nextLine();//每次输入一个命令
			if(command.contains("Append"))//增加的命令
			{
				String temp = command.split(" ")[1];//用空格将命令拆分开，去除后面需要增加的字符串
				str = sb.append(temp).toString();//把字符串增加上，在赋值给str
			}
			else if(command.contains("Insert"))//插入命令
			{
				int index = Integer.parseInt(command.split(" ")[1]);//获取插入的位置
				String temp = command.split(" ")[2];//获取插入的字符串
				str = sb.insert(index-1, temp).toString();//利用StringBuilder对象的insert方法进行插入，因为题目是从1开始计算下角标的，所以这里index-1
			}
			else if(command.contains("Swap"))//交换命令
			{
				String strs[] = command.split(" ");//拆分命令获取abcd四个位置
				int a = Integer.parseInt(strs[1]);
				int b = Integer.parseInt(strs[2]);
				int c = Integer.parseInt(strs[3]);
				int d = Integer.parseInt(strs[4]);
				
				String s1 = str.substring(a-1,b);//获取a到b的字符串
				String s2 = str.substring(c-1,d);//获取c到d的字符串
				
				/*
				 * 这里比较抽象
				 * 要交换a到b和c到d （从0开始算下角标应该是a-1到b-1  c-1到d-1）
				 *  1.所以首先把a-1之前的保留
				 *  2.s2就是c-1到d-1直接加在第一步的后面
				 *  3.s2已经加在了后面，所以就不要a-1到b-1了，那就把从b开始到c-1的内容保留,这样a-1到b-1就被s2替换 了
				 *  4.然后接下来原来应该是 c-1到d-1所以用s1加在后面
				 *  5.最后把原来d-1之后的保留就完成了两部分交换
				 */		
				str = str.substring(0,a-1) + s2 +str.substring(b, c-1) + s1 + str.substring(d);
				sb = new StringBuilder(str);
			}
			else//反转命令
			{
				String strs[] = command.split(" ");//拆分命令获取ab两个个位置
				int a = Integer.parseInt(strs[1]);
				int b = Integer.parseInt(strs[2]);
				
				String s1 = str.substring(a-1, b);//获取a-1到b的子串
				s1 = new StringBuilder(s1).reverse().toString();//将子串反转获取反转后的结果
				sb.replace(a-1, b, s1);//这里因为反转后长度一定一样，所以直接使用StringBuilder对象的replace方法替换原来正序的位置就行了
				str = sb.toString();//把反转后的结果返回给str
			}
		}
		
		System.out.println(str);
		sc.close();
	}

}
