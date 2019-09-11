import React from "react";
import {
  AsyncStorage,
  Button,
  StyleSheet,
  View,
  Text,
  TextInput
} from "react-native";
import { Input, ThemeProvider, CheckBox, Image } from "react-native-elements";
import Icon from "react-native-vector-icons/FontAwesome";
import LogoTitle from "../../components/LogoTitle"

export default class SignInScreen extends React.Component {
  state = {
    username: "",
    usernameError: null,
    password: "",
    passwordError: null,
    rememberMe: false
  };
  static navigationOptions = {
    headerTitle: <LogoTitle title="Please sign in"/>
  };

  render() {
    return (
      <ThemeProvider>
        <View style={{ flex: 1, padding: 100 }}>
          <View
            style={{ flex: 1, flexDirection: "column", alignItems: "center" }}
          >
            <Image
              source={
                __DEV__
                  ? require('../../assets/images/nyan1.svg')
                  : require('../../assets/images/nyan1.svg')
              }
              style={this.styles.welcomeImage}
            />
            <Text style={{ fontSize: 27 }}>Log in to Twitter Clone</Text>
            <Input
              label="Username"
              autoFocus={true}
              errorMessage={this.state.usernameError}
              leftIcon={
                <Icon
                  name="user"
                  size={24}
                  color="black"
                  style={{ paddingRight: 2 }}
                />
              }
              textContentType="username"
              onChangeText={value => this.setState({ username: value })}
              value={this.state.value}
            />
            <Input
              label="Password"
              errorMessage={this.state.passwordError}
              textContentType="password"
              autoCompleteType="password"
              secureTextEntry={true}
              onChangeText={value => this.setState({ password: value })}
            />
            <View style={{ margin: 7 }} />

            <View style={{flexDirection: "column"}}>
              <View
                style={{
                  flex: 1,
                  flexDirection: "row",
                  justifyContent: "center",
                  alignItems: "baseline"
                }}
              >
                <Button
                  title="Log in!"
                  style={{ flex: 2 }}
                  onPress={this._signInAsync}
                />
                <CheckBox
                  style={{ flex: 1 }}
                  checked={this.state.rememberMe}
                  onPress={() =>
                    this.setState({ rememberMe: !this.state.rememberMe })
                  }
                  title="Remember me"
                ></CheckBox>
                <Text
                  style={{ color: "blue", flex: 1 }}
                  onPress={() =>
                    this.props.navigation.navigate("ForgotPassword")
                  }
                >
                  Forgot Password
                </Text>
              </View>

              <View
                style={{
                  flex: 1,
                  flexDirection: "row",
                  justifyContent: "center",
                  alignItems: "center"
                }}
              >
                <Text style={{ flex: 1 }}>New to Twitter Clone? </Text>
                <Text
                  style={{ color: "blue" }}
                  onPress={() => this.props.navigation.navigate("Signup")}
                >
                  Sign up now!
                </Text>
              </View>
            </View>
          </View>
        </View>
      </ThemeProvider>
    );
  }

  _signInAsync = async () => {
    let errorObject = {};
    if (this.state.username.length == 0)
      errorObject.usernameError = "Please, inform a valid username";
    else errorObject.usernameError = null;
    if (this.state.password.length == 0)
      errorObject.passwordError = "Please, inform a valid password";
    else errorObject.passwordError = null;
    if (errorObject.usernameError != null || errorObject.passwordError != null)
      this.setState(errorObject);
    else {
      this.setState({ usernameError: null, passwordError: null });
      await AsyncStorage.setItem("userToken", "abc");
      this.props.navigation.navigate("App");
    }
  };

  styles = {
  welcomeImage: {
    width: 120,
    height: 80,
    resizeMode: 'contain',
    marginTop: 3,
    marginLeft: -10,
  }
  }
}
