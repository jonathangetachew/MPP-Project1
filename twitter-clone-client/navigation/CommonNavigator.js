import { createSwitchNavigator } from 'react-navigation';
import { createStackNavigator } from 'react-navigation-stack';
import SignInScreen from '../screens/auth/SignInScreen';
import AuthLoadingScreen from '../screens/auth/AuthLoadingScreen';

import MainTabNavigator from './MainTabNavigator';
const AuthStack = createStackNavigator({ SignIn: SignInScreen });

export default createSwitchNavigator({
    // You could add another route here for authentication.
    // Read more at https://reactnavigation.org/docs/en/auth-flow.html
    AuthLoading: AuthLoadingScreen,
    App: MainTabNavigator,
    Auth: AuthStack,
  },
  {
    initialRouteName: 'AuthLoading',
  });