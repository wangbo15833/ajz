package com.v_info.ajzclientv3.service;

public class Tools {
	/**
	 * 
	 * @param s
	 * @param s1
	 * @return if s=null then return s1
	 */
	public static String nvl(String s, String s1)
{
	return s != null && !s.equals("null") ? s : s1;
}

	/**
	 * 
	 * @param s
	 * @return if s==null return ""
	 */
	public static String nvl(String s)
	{
		return nvl(s, "").trim();
	}

	/**
	 * 
	 * @param s
	 * @return if s==null return "0"
	 */
	public static String nvl0(String s)
	{
		return s != null && !s.equals("null") && !s.equals("") ? s : "0";
	}
	/**
	 * 
	 * @param s
	 * @param integer t
	 * @return omit s with ...
	 */
	public static String omit(String s,Integer t)
		{
			String s1="";
			try
			{
				if(t==0){
					return nvl(s);
				}
				s=nvl(s);
				s=s.replaceAll("\n","");
				s=s.replaceAll("\r","");
				s1=nvl(s).length()>t?s.substring(0,t)+"...":s;
			}
			catch (Exception exception)
			{
				exception.printStackTrace();
			}
			return s1;
		}
		/**
		 * 
		 * @param s
		 * @param t
		 * @return without ...
		 */
			public static String omitnd(String s,Integer t)
		{
			String s1="";
			try
			{
				if(t==0){
					return nvl(s);
				}
				s=nvl(s);
				s=s.replaceAll("\n","");
				s=s.replaceAll("\r","");
				s1=nvl(s).length()>t?s.substring(0,t):s;
			}
			catch (Exception exception)
			{
				exception.printStackTrace();
			}
			return s1;
		}
		/**
		 * 
		 * @param s
		 * @param s2
		 * @return with ...
		 */
			public static String omit(String s,String s2)
		{
			int t =0;
			String s1="";
			try
			{
				String s3="...";
				if(((nvl0(s2)).replaceAll(" ","")).indexOf("'")>0){s3="";}
				t = Integer.parseInt(((nvl0(s2)).replaceAll(" ","")).equals("")?"0":((nvl0(s2)).replaceAll(" ","").replaceAll("'","")));
				if(t==0){
					return nvl(s);
				}
				s=nvl(s);
				s=s.replaceAll("\n","");
				s=s.replaceAll("\r","");
				s1=nvl(s).length()>t?s.substring(0,t)+s3:s;
			}
			catch (Exception exception)
			{
				exception.printStackTrace();
			}
			return s1;
		}
}
