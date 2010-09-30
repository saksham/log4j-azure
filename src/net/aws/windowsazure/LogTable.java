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

import java.net.URI;
import org.soyatec.windowsazure.table.ITable;
import org.soyatec.windowsazure.table.TableServiceContext;
import org.soyatec.windowsazure.table.TableStorageClient;

public class LogTable {

    private TableServiceContext tableServiceContext;
	
    public LogTable(String accountName, String password, String tableName) {
		
        TableStorageClient storage = TableStorageClient.create(
                                       URI.create("http://table.core.windows.net"),
                                       false,
                                       accountName,
                                       password);

        ITable table = storage.getTableReference(tableName);

        if(!table.isTableExist())
                table.createTable();

        tableServiceContext = table.getTableServiceContext();

    }
	
    public void insert(String partitionKey, String rowKey, String message) {

        LogEntity logEntity = new LogEntity(partitionKey, rowKey);
        logEntity.setMessage(message);
        tableServiceContext.insertEntity(logEntity);
	
    }
}
