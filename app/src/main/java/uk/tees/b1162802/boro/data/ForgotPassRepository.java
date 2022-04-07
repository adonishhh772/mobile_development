package uk.tees.b1162802.boro.data;


public class ForgotPassRepository {
    private static volatile ForgotPassRepository instance;

    private ForgotPassDataSource dataSource;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore

    // private constructor : singleton access
    private ForgotPassRepository(ForgotPassDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static ForgotPassRepository getInstance(ForgotPassDataSource dataSource) {
        if (instance == null) {
            instance = new ForgotPassRepository(dataSource);
        }
        return instance;
    }

    public Result<String> forgotPass(String username) {
        // handle reset password
        Result<String> result = dataSource.forgotPass(username);
        if (result instanceof Result.Success) {
//            setLoggedInUser(((Result.Success<LoggedInUser>) result).getData());
        }
        return result;
    }
}
