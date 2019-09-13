import React from "react";
import { Image, Button, KeyboardAvoidingView, View, Text, Platform, ScrollView } from "react-native";
import { Header } from "react-navigation";
import { Input } from "react-native-elements";
import Icon from "react-native-vector-icons/FontAwesome";
import Colors from "../../constants/Colors";
import RoundButton from "../../components/RoundButton";
import LogoTitle from "../../components/LogoTitle"
import DatePicker from 'react-native-datepicker'

const Label = (props) => (
  <Text style={{fontSize: 16, color: Colors.labelColor, fontWeight: 'bold'}}>{props.children}</Text>
);

const LoginHeader = (props) => (
  <View
    style={{
      flex:1,
      alignItems: "center"
    }}
  >
    <Image
      source={require("../../../assets/images/logo.png")}
      style={{
        resizeMode: "contain",
        width: 25,
        height: 25,
        marginHorizontal: 20
      }}
    />
  </View>
);

export default class SignUpScreen extends React.Component {
  state = {
    userData: {
      username: "",
      email: "",
      birthDate: new Date(),
      password: "",
      confirmPassword: "",
    },
    error: {
      username: null,
      email: null,
      birthDate: null,
      password: null
    }
  };
  static navigationOptions = ({ navigation }) => ({
    headerTitle: <LoginHeader/>,
    header: Platform.OS != "web" ? undefined : null
  });

  render() {
    return (
      <KeyboardAvoidingView
        keyboardVerticalOffset={Header.HEIGHT + 20}
        style={{ backgroundColor: "red" }}
        behavior="padding"
        enabled
      >
        <ScrollView>
          <View
            style={{
              flex: 1,
              backgroundColor: "white",
              alignItems: "stretch",
              padding: Platform.OS == "web" ? undefined : "20%",
              paddingHorizontal: Platform.OS == "web" ? "40%" : undefined
            }}
          >
            <View
              style={{
                flexDirection: "column",
                alignItems: "stretch"
              }}
            >
              <Text style={{ fontSize: 27, marginBottom: 20 }}>
                Create Your account
              </Text>
              <Input
                label="Username"
                autoFocus={true}
                errorMessage={this.state.error.username}
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
                onChangeText={value => this.setState({ userData: {...this.state.userData, username: value} })}
                value={this.state.userData.username}
              />
              <Input
                label="Email address"
                errorMessage={this.state.error.email}
                leftIcon={
                  <Icon
                    name="envelope"
                    size={24}
                    color={Colors.primaryColor}
                    style={{ marginRight: 8 }}
                  />
                }
                onChangeText={value => this.setState({ userData: {...this.state.userData, email: value }})}
                value={this.state.userData.email}
              />
              <Input
                label="Password"
                leftIcon={
                  <Icon
                    name="lock"
                    size={24}
                    color={Colors.primaryColor}
                    style={{ marginRight: 8 }}
                  />
                }
                textContentType="password"
                secureTextEntry={true}
                onChangeText={value => this.setState({ userData: {...this.state.userData, password: value} })}
              />
              <Input
                label="Confirm Password"
                errorMessage={this.state.error.password}
                leftIcon={
                  <Icon
                    name="lock"
                    size={24}
                    color={Colors.primaryColor}
                    style={{ marginRight: 8 }}
                  />
                }
                textContentType="password"
                secureTextEntry={true}
                onChangeText={value => this.setState({ userData: {...this.state.userData, confirmPassword: value} })}
              />
              <Label>Date Of Birth</Label>
              <DatePicker maxDate={new Date()} date={this.state.userData.birthDate} onDateChange={(date) => {this.setState({userData: {...this.state.userData, birthDate: date}})}}/>
              <View style={{ margin: 7 }} />

              <View
                style={{
                  justifyContent: "center",
                  alignItems: "stretch"
                }}
              >
                <RoundButton title="Sign up" onPress={this._signup} />
              </View>
            </View>
          </View>
        </ScrollView>
      </KeyboardAvoidingView>
  
    );
  }

  // validateEmail function
  // copied from https://github.com/react-native-elements/react-native-elements-app/blob/master/src/views/login/screen1.js
  validateEmail(email) {
    var re = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;

    return re.test(email);
  }

  _signup = () => {
    let errorObject = {};
    if (this.state.userData.username.length == 0)
      errorObject.username = "Please don't try to think outside the box in this system.";
    else
      errorObject.username = null;
    if (this.state.userData.email.length == 0 || !this.validateEmail(this.state.userData.email))
      errorObject.email =
        "We need a valid email to create your account.";
    else
      errorObject.email = null;
    if (this.state.userData.password != this.state.userData.confirmPassword)
      errorObject.password =
        "The passwords don't match.";
    else if(this.state.userData.password.length < 6)
      errorObject.password =
        "Please, make sure your password is at least 6 characters long.";
    else
      errorObject.password = null;
    if (errorObject.username != null || errorObject.email != null || errorObject.password != null)

      this.setState({error:errorObject});
    else {
      console.log(this.state.userData);
      this.props.navigation.navigate("SignIn");
    }
  };
}
