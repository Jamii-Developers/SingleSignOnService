public class UserLogin {

    private UserLoginStruct userloginstruct = new UserLoginStruct( );
    
    public UserLogin( UserLoginStruct userloginstruct ){
        this.userloginstruct = userloginstruct;
    }

    public String getResponse( )(
        return userloginstruct.getRESPONSE( );
    )


}
