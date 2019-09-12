import { createStackNavigator } from 'react-navigation-stack';
import SignInScreen from './SignInScreen';
import ForgotPasswordScreen from './ForgotPasswordScreen';
import WelcomeScreen from './WelcomeScreen';

const AuthStack = createStackNavigator({
    Welcome: WelcomeScreen,
    SignIn: SignInScreen,
    ForgotPassword: ForgotPasswordScreen
}, {
    initialRouteName: 'Welcome',
});

//AuthStack.path = '';

export default AuthStack;