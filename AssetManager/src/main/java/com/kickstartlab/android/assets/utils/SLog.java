package com.kickstartlab.android.assets.utils;

import com.kickstartlab.android.assets.rest.models.MemberData;
import com.kickstartlab.android.assets.rest.models.ScanLog;
import com.orm.query.Select;

/**
 * Created by awidarto on 3/30/15.
 */

public class SLog {

    public SLog(){

    }

    public static void s(String result, String message, String assettype, String extId ){

        MemberData actor = Select.from(MemberData.class).first();

        String logId = RandomStringGenerator.generateRandomString(15, RandomStringGenerator.Mode.HEX);

        ScanLog s = new ScanLog();
        String timestamp = DbDateUtil.getDateTime();
        s.setAction("scan");
        s.setTimestamp(timestamp);
        s.setLogMessage(message);
        s.setResult(result);
        s.setActor( actor.getFullname() + " : " + actor.getEmail() );
        s.setAssetType(assettype);
        s.setExtId(extId);
        s.setLogId(logId);
        s.setActorId(actor.getMongoid());
        s.setUploaded(0);
        s.save();
    }

    public static void e(String result, String message, String assettype){
        MemberData actor = Select.from(MemberData.class).first();

        String logId = RandomStringGenerator.generateRandomString(15, RandomStringGenerator.Mode.HEX);

        ScanLog s = new ScanLog();
        String timestamp = DbDateUtil.getDateTime();
        s.setAction("scan");
        s.setTimestamp(timestamp);
        s.setLogMessage(message);
        s.setActor( actor.getFullname() + " : " + actor.getEmail() );
        s.setResult(result);
        s.setAssetType(assettype);
        s.setActorId(actor.getMongoid());
        s.setLogId(logId);
        s.setUploaded(0);
        s.save();
    }

}
