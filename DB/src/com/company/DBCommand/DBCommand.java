package com.company.DBCommand;

import com.company.DBExceptions.StorageType;

public interface DBCommand {
    int getIndex();
    StorageType getType();
}
