package cn.test.gk;

import java.util.Scanner;
/**
 * 算法训练      2的次幂表示
 * 资源限制
	时间限制：1.0s   内存限制：512.0MB
	问题描述
	　　任何一个正整数都可以用2进制表示，例如：137的2进制表示为10001001。
	　　将这种2进制表示写成2的次幂的和的形式，令次幂高的排在前面，可得到如下表达式：137=2^7+2^3+2^0
	　　现在约定幂次用括号来表示，即a^b表示为a（b）
	　　此时，137可表示为：2（7）+2（3）+2（0）
	　　进一步：7=2^2+2+2^0 （2^1用2表示）
	　　3=2+2^0
	　　所以最后137可表示为：2（2（2）+2+2（0））+2（2+2（0））+2（0）
	　　又如：1315=2^10+2^8+2^5+2+1
	　　所以1315最后可表示为：
	　　2（2（2+2（0））+2）+2（2（2+2（0）））+2（2（2）+2（0））+2+2（0）
	输入格式
	　　正整数（1<=n<=20000）
	输出格式
	　　符合约定的n的0，2表示（在表示中不能有空格）
	样例输入
	137
	样例输出
	2(2(2)+2+2(0))+2(2+2(0))+2(0)
	样例输入
	1315
	样例输出
	2(2(2+2(0))+2)+2(2(2+2(0)))+2(2(2)+2(0))+2+2(0)
 * @author 郭凯
 *
 */
public class Main04 {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		int n = sc.nextInt();
		
		String str = f(n);
		
		int index = 0;
		while((index = ok(str))!=-1)
		{
			int num = Integer.parseInt(findNoEqual2(index, str));
			String temp = f(num);
			str = str.substring(0,index)+ temp + str.substring(findNoEqual2Index(index,str));
		}
		System.out.println(str);
		sc.close();
	}
	
	/**
	 * 把str中从index开始到下一个')'的下标返回
	 * @param index
	 * @param str
	 * @return
	 */
	private static int findNoEqual2Index(int index, String str) {
		
		for(int i=index;i<str.length();i++)
		{
			if(str.charAt(i)  == ')')
			{
				return i;
			}
		}
		return 0;
	}

	/**
	 * 将数字n变成这种形式   137  -->   2（7）+2（3）+2（0）
	 * @param n
	 */
	public static String f(int n)
	{
		String str = Integer.toBinaryString(n);
		String res = "";
		for(int i=0;i<str.length();i++)
		{
			if(str.charAt(i)=='1'){
				res += "2("+(str.length()-i-1)+")+";
			}
		}
		
		return res.substring(0, res.length()-1).replaceAll("\\(1\\)", "");
	}
	
	/**
	 * 判断字符串中是不是只包括'2','0', '(', ')' 和'+'
	 * @param s
	 * @return
	 */
	public static int ok(String s)
	{
		for(int i=0;i<s.length();i++)
		{
			if(s.charAt(i)!='2'&&s.charAt(i)!='0'&&s.charAt(i)!='+'&&s.charAt(i)!='('&&s.charAt(i)!=')')
			{
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * 把str中从index开始到下一个')'中间的内容返回
	 * @return
	 */
	public static String findNoEqual2(int index,String str){
		
		String s = "";
		for(int i=index;;i++){
			if(str.charAt(i)!=')'){
				s+=str.charAt(i);
			}else{
				break;
			}
		}
		
		return s;
	}
}
