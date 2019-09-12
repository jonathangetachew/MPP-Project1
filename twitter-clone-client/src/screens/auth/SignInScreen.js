import React from "react";
import {
  AsyncStorage,
  View,
  ScrollView,
  KeyboardAvoidingView,
  Text,
} from "react-native";
import {
  Input,
  CheckBox,
  Image
} from "react-native-elements";
import RoundButton from "../../components/RoundButton";
import Colors from "../../constants/Colors";
import Icon from "react-native-vector-icons/FontAwesome";
import imageLogo from "../../../assets/images/logo.png";

export default class SignInScreen extends React.Component {
  state = {
    username: "",
    usernameError: null,
    password: "",
    passwordError: null,
    rememberMe: false
  };
  static navigationOptions = {
    header: null
  };

  render() {
    return (
      <ScrollView contentContainerStyle={this.styles.container}>
        <View style={this.styles.container}>
        <Image source={imageLogo} style={this.styles.logo} />
        <KeyboardAvoidingView style={this.styles.form}>
          <Text style={{ fontSize: 27, marginBottom: 20 }}>Log in to TwitterClone</Text>
          <Input
            label="Username"
            autoFocus={true}
            errorMessage={this.state.usernameError}
            leftIcon={
              <Icon
                name="user"
                size={24}
                color={Colors.primaryColor}
                style={{ marginRight: 8 }}
              />
            }
            maxLength={20}
            textContentType="username"
            onChangeText={value => this.setState({ username: value })}
            value={this.state.value}
          />
          <Input
            label="Password"
            errorMessage={this.state.passwordError}
            leftIcon={
              <Icon
                name="lock"
                size={24}
                color={Colors.primaryColor}
                style={{ marginRight: 8 }}
              />
            }
            textContentType="password"
            autoCompleteType="password"
            secureTextEntry={true}
            onChangeText={value => this.setState({ password: value })}
          />
          <View style={{ margin: 7 }} />

          <View style={{ flexDirection: "column" }}>
            <View
              style={{
                flex: 1,
                flexDirection: "row",
                justifyContent: "center",
                alignItems: "baseline"
              }}
            >
              <RoundButton
                title="Log in!"
                style={{ flex: 2 }}
                onPress={this._signInAsync}
              />
              <CheckBox
                style={{ flex: 1 }}
                checked={this.state.rememberMe}
                onPress={() =>
                  this.setState({
                    rememberMe: !this.state.rememberMe
                  })
                }
                title="Remember me"
              ></CheckBox>
            </View>

            <View
              style={{
                flex: 1,
                flexDirection: "row",
                justifyContent: "center",
                alignItems: "center"
              }}
            >
              <Text style={this.styles.spacedThingyForJonathansEyes}>New to TwitterClone? </Text>
              <Text
                style={{ color: "blue" }}
                onPress={() => this.props.navigation.navigate("Signup")}
              >
                Sign up
              </Text>
            </View>
          </View>
        </KeyboardAvoidingView>
        </View>
      </ScrollView>
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
      this.setState({
        usernameError: null,
        passwordError: null
      });
      await AsyncStorage.setItem("userToken", "abc");
      this.props.navigation.navigate("App");
    }
  };

  styles = {
    moreSpacedThingyForJonathansEyes: {
      marginTop: 15,
      marginBottom: 15
    },
    spacedThingyForJonathansEyes: {
      marginTop: 15,
      marginBottom: 15
    },
    container: {
      flex: 1,
      backgroundColor: "white",
      alignItems: "center",
      justifyContent: "space-between",
      marginTop: 30 //"3rem"
    },
    form: {
      flex: 1,
      flexDirection: "column",
      alignItems: "center"
    },
    logo: {
      width: 95,
      height: 80,
      resizeMode: "contain",
      marginTop: 30
      //marginLeft: -10,
    }
  };
}
