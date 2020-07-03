package cn.test.gk;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * 题目：算法训练 审美课
 * 资源限制
	时间限制：1.0s   内存限制：256.0MB
	问题描述
	　　《审美的历程》课上有n位学生，帅老师展示了m幅画，其中有些是梵高的作品，另外的都出自五岁小朋友之手。老师请同学们分辨哪些画的作者是梵高，但是老师自己并没有答案，因为这些画看上去都像是小朋友画的……老师只想知道，有多少对同学给出的答案完全相反，这样他就可以用这个数据去揭穿披着皇帝新衣的抽象艺术了（支持帅老师^_^）。
	　　答案完全相反是指对每一幅画的判断都相反。
	输入格式
	　　第一行两个数n和m，表示学生数和图画数；
	　　接下来是一个n*m的01矩阵A：
	　　如果aij=0，表示学生i觉得第j幅画是小朋友画的；
	　　如果aij=1，表示学生i觉得第j幅画是梵高画的。
	输出格式
	　　输出一个数ans：表示有多少对同学的答案完全相反。
	样例输入
	3 2
	1 0
	0 1
	1 0
	样例输出
	2
	样例说明
	　　同学1和同学2的答案完全相反；
	　　同学2和同学3的答案完全相反；
	　　所以答案是2。
	数据规模和约定
	　　对于50%的数据：n<=1000；
	　　对于80%的数据：n<=10000；
	　　对于100%的数据：n<=50000，m<=20。
 * @author 郭凯
 *
 */
public class Main02 {
	
	public static void main(String[] args) 
	{
		Scanner sc = new Scanner(System.in);
		
		int n = sc.nextInt();//有n个学生
		int m = sc.nextInt();//有m幅画
		
		//按照题目要求，可能最多会有50000学生，用数组A记录每一个学生的01所组成的值
		int A[] = new int[50005];
		
		//使用map存储每种判断出现的次数，map中的key存的是这种判断的十进制数组，value存的是这种判断出现的次数
		Map<Integer,Integer> map = new HashMap<Integer,Integer>();
		
		int cnt = 0;
		
		for(int i=0;i<n;i++)//按照行进行输入
		{
			for(int j=0;j<m;j++)
			{
				int temp = sc.nextInt();//输入i行j列的值
				A[i] = (A[i]<<1)+temp;//然后用以为的方式计算出这一行01组成的二进制数值所对应的十进制的值
			}
			if(map.containsKey(A[i])){//如果map里面有这个key了，就把这种判断出现的次数取出后加1在存回去
				int num = map.get(A[i]) + 1;
				map.put(A[i], num);
			}else{//没有的话就直接放进去，次数初始化为1
				map.put(A[i], 1);
			}
		}
		
		int q = (1<<m)-1;//每行有m列，咱们做取反运算，就要做出二进制表示是m个1的数，让他与A数组中的每一种判断作异或
		for(int i=0;i<n;i++)
		{
			int temp = A[i] ^ q;
			if(map.containsKey(temp)){
				cnt += map.get(temp);
			}
		}
		
		System.out.println(cnt/2);
		
		sc.close();
	}

}
/**
二进制取反：
	用二进制存储的思路如下：
	
	1.将每个学生的答案用数组A[i]以二进制的形式存储。故答案相同的学生数组A[i]存的值是相同的。
	2.数组ans[ A[i] ]用于存储每种答案的人数。例如，假设ans[3]=10，即有10个人答案相同且答案都为3 (十进制3对应的二进制为011)。
	3.按行遍历，按位取反，与取反后的答案相同的 即为题目要求的完全相反的答案。
	4.最后sum/2是因为重复计算了，除以2之后才是“有多少对同学”。
*/