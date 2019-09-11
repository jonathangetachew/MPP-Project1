import React from "react";
import { AsyncStorage, Button, StyleSheet, View,Text } from "react-native";
import { Input, ThemeProvider } from "react-native-elements";
import Icon from "react-native-vector-icons/FontAwesome";

export default class SignInScreen extends React.Component {
  state = {
    username: "",
    usernameError: null,
    password: "",
    passwordError: null,
  };
  static navigationOptions = {
    title: "Please sign in"
  };

  render() {
    return (
      <ThemeProvider>
        <Text style={{ fontSize: 27 }}>Log in to Twitter Clone</Text>
        <Input
          label="Username"
          autoFocus={true}
          errorMessage={this.state.usernameError}
          leftIcon={<Icon name="user" size={24} color="black" style={{paddingRight: 2}}/>}
          textContentType="username"
          onChangeText={value => this.setState({ username: value })} value={this.state.value}
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

        <Button title="Log in!" onPress={this._signInAsync} />
      </ThemeProvider>
    );
  }

  _signInAsync = async () => {
    let errorObject = {};
    if(this.state.username.length == 0)
      errorObject.usernameError = "Please, inform a valid username";
    else
      errorObject.usernameError = null;
    if(this.state.password.length == 0)
      errorObject.passwordError = "Please, inform a valid password";
    else
      errorObject.passwordError = null;
    if(errorObject.usernameError != null || errorObject.passwordError != null)
      this.setState(errorObject);
    else {
      this.setState({usernameError: null, passwordError: null})
      await AsyncStorage.setItem("userToken", "abc");
      console.log(this.state);
      this.props.navigation.navigate("App");
    }
  };
}
