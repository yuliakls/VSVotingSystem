package com.example.katri.vsvotingsystem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Voting {

    private int VoteNum;

    public String getVoteName() {
        return VoteName;
    }

    public void setVoteName(String voteName) {
        VoteName = voteName;
    }

    public String getVoteDescription() {
        return VoteDescription;
    }

    public void setVoteDescription(String voteDescription) {
        VoteDescription = voteDescription;
    }

    public Date getStart() {
        return Start;
    }

    public void setStart(Date start) {
        this.Start = start;
    }

    public Date getFinish() {
        return Finish;
    }

    public void setFinish(Date finish) {
        this.Finish = finish;

    }

    private String VoteName;
    private String VoteDescription;
    private Date Start;
    private Date Finish;


    public Voting(int VoteNum,String VoteName,String VoteDescription,String Start,String Finish) throws ParseException {
        this.VoteNum = VoteNum;
        this.VoteName = VoteName;
        this.VoteDescription = VoteDescription;
        this.Start = Parse( Start );
        this.Finish = Parse(Finish);

    }

    public static Date Parse( String input ) throws java.text.ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date date = formatter.parse(input.replaceAll("Z$", "+0000"));

        //System.out.println(date);
        //System.out.println("time zone : " + TimeZone.getDefault().getID());
        //System.out.println(formatter.format(date));
        return date;
    }

    public static String toString( Date date ) {

        SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss" );

        TimeZone tz = TimeZone.getTimeZone( "UTC" );

        df.setTimeZone( tz );

        String output = df.format( date );

        int inset0 = 9;
        int inset1 = 6;

        String s0 = output.substring( 0, output.length() - inset0 );
        String s1 = output.substring( output.length() - inset1, output.length() );

        String result = s0 + s1;

        result = result.replaceAll( "UTC", "+00:00" );

        return result;

    }



}
