package cn.test2.gk;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * 试题 算法提高 字符串压缩
 *   问题描述
	　　编写一个程序，输入一个字符串，然后采用如下的规则对该字符串当中的每一个字符进行压缩：
	　　(1) 如果该字符是空格，则保留该字符；
	　　(2) 如果该字符是第一次出现或第三次出现或第六次出现，则保留该字符；
	　　(3) 否则，删除该字符。
	　　例如，若用户输入“occurrence”，经过压缩后，字符c的第二次出现被删除，第一和第三次出现仍保留；字符r和e的第二次出现均被删除，因此最后的结果为：“ocurenc”。
	　　输入格式：输入只有一行，即原始字符串。
	　　输出格式：输出只有一行，即经过压缩以后的字符串。
	输入输出样例
	样例输入
		occurrence
	样例输出
		ocurenc
 * 
 * @author 郭凯
 *
 */
public class Main02 {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		String str = sc.nextLine();
		
		Map<Character,Integer> map = new HashMap<Character,Integer>();
		
		char[] arr = str.toCharArray();
		for(int i=0;i<str.length();i++){
			char c = str.charAt(i);
			if(c == ' '){
				continue;
			}else{
				if(!map.containsKey(c)){
					map.put(c, 1);
				}else{
					Integer cnt = map.get(c);
					cnt = cnt+1;
					map.put(c, cnt);
					if(cnt!=1 && cnt!=3 && cnt!=6){
						arr[i] = '*';
					}
				}
			}
		}
		String ss = "";
		for(char c : arr){
			if(c != '*'){
				ss += c;
			}
		}
		System.out.println(ss);
		sc.close();
		
	}

}
