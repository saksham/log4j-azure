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

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.sql.Timestamp;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.Level;
import org.apache.log4j.Priority;

public class AzureTableAppender extends AppenderSkeleton
{
	private String tableStorageEndpoint;
    private String accountName;
    private String accountKey;
    private String tableName = "Logs";
    private LogTable logTable = null;

    public AzureTableAppender() {
	    super();
    } 

    public boolean requiresLayout() { return true; }

    public synchronized void append(LoggingEvent loggingEvent) {

        // Temporarily switch off logging to avoid infinite recursion
        // (The underlying Azure library also uses log4j !)
        Priority priority = this.getThreshold();
        this.setThreshold(Level.OFF);

        String message = this.layout.format(loggingEvent);

        if (this.logTable == null) {
        	//When using development storage account, use path-style URIs
        	boolean usePathStyleUris = getTableStorageEndpoint().contains("//127.0.0.1:10002");
            this.logTable = new LogTable(getTableStorageEndpoint(), 
            		getAccountName(), getAccountKey(), usePathStyleUris,
            		getTableName());
        }

        //Construct row key based on the reversed timestamp
        Calendar calendar = Calendar.getInstance();
        Timestamp max = new Timestamp(new Date(Long.MAX_VALUE).getTime());
        String rowKey = String.valueOf(max.getTime() - calendar.getTimeInMillis());
        
        //Change the calendar to today's date to compute partition key
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        String partitionKey = String.valueOf(max.getTime() - calendar.getTimeInMillis());
        
        String level = loggingEvent.getLevel().toString();

        this.logTable.insert(partitionKey, rowKey, message, level);

        // Switch logging back on
        this.setThreshold(priority);
    }
    
    public synchronized void close() {
	
    }

    public String getTableStorageEndpoint() {
    	return this.tableStorageEndpoint;
    }

    public void setTableStorageEndpoint(String tableStorageEndpoint) {
    	this.tableStorageEndpoint = tableStorageEndpoint;
    }
    
    public String getAccountName() {
        return this.accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountKey() {
        return this.accountKey;
    }

    public void setAccountKey(String accountKey) {
        this.accountKey = accountKey;
    }

    public String getTableName() {
        return this.tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
