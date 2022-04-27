package uk.tees.b1162802.boro.data;


import java.util.Map;

import uk.tees.b1162802.boro.data.model.LoggedInUser;

public class RegisterRepository {
    private static volatile RegisterRepository instance;

    private RegisterDataSource dataSource;
    private LoggedInUser user = null;

    // private constructor : singleton access
    private RegisterRepository(RegisterDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static RegisterRepository getInstance(RegisterDataSource dataSource) {
        if (instance == null) {
            instance = new RegisterRepository(dataSource);
        }
        return instance;
    }


    public Result<LoggedInUser> register(String uid,Map<String,String> registerData) {
        // handle register
        Result<LoggedInUser> result = dataSource.register(uid,registerData);
        if (result instanceof Result.Success) {
            setRegisteredUser(((Result.Success<LoggedInUser>) result).getData());
        }
        return result;
    }

    private void setRegisteredUser(LoggedInUser data) {
        this.user = data;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}
