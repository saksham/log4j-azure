/*

(C) Copyright 2010 Active Web Solutions Ltd
All rights reserved.

This software is provided "as is" without warranty of any kind,
express or implied, including but not limited to warranties as to
quality and fitness for a particular purpose. Active Web Solutions Ltd
does not support the Software, nor does it warrant that the Software
will meet your requirements or that the operation of the Software will
be uninterrupted or error free or that any defects will be
corrected. Nothing in this statement is intended to limit or exclude
any liability for personal injury or death caused by the negligence of
Active Web Solutions Ltd, its employees, contractors or agents.

*/

package net.aws.windowsazure;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.soyatec.windowsazure.table.AbstractTableServiceEntity;

public class LogEntity extends AbstractTableServiceEntity {
	
    public LogEntity(String partitionKey, String rowKey) {
        super(partitionKey, rowKey);
    }

    private String Message;
    
    public String getMessage ()        
    {            
    	return Message;        
    }

    public void setMessage (String message)        
    {            
    	this.Message = message;                   
    }

    private String Level;
    
    public String getLevel()
    {
    	return this.Level;
    }
    
    public void setLevel(String level) {
    	this.Level = level;
    }
    
    public Date getLogTime() {
    	Timestamp reversedTimestamp = new Timestamp(Long.valueOf(getRowKey())); 
    	Timestamp maxTimestamp = new Timestamp(new Date(Long.MAX_VALUE).getTime());
    	return new Date(maxTimestamp.getTime() - reversedTimestamp.getTime());
    }
    
}

