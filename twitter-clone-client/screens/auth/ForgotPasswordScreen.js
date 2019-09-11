import React from "react";
import { AsyncStorage, Button, StyleSheet, View, Text } from "react-native";
import { Input, ThemeProvider, CheckBox } from "react-native-elements";
import Icon from "react-native-vector-icons/FontAwesome";
import LogoTitle from "../../components/LogoTitle"

export default class ForgotPasswordScreen extends React.Component {
  state = {
    username: "",
    usernameError: null
  };
  static navigationOptions = {
    headerTitle: <LogoTitle title="Password Reset"/>
  };

  render() {
    return (
      <ThemeProvider>
        <View style={{ flex: 1, padding: 100 }}>
          <View
            style={{ flex: 1, flexDirection: "column", alignItems: "center" }}
          >
            <Text style={{ fontSize: 27 }}>
              Find your Twitter Clone account
            </Text>
            <Input
              label="Enter your username"
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
            <View style={{ margin: 7 }} />

            <Button title="Search" onPress={this._search} />
          </View>
        </View>
      </ThemeProvider>
    );
  }

  _search = async () => {
    let errorObject = {};
    if (this.state.username.length == 0)
      errorObject.usernameError =
        "We need this information to find your account.";
    if (errorObject.usernameError != null) this.setState(errorObject);
    else {
      this.setState({ usernameError: null });
      this.props.navigation.navigate("Auth");
    }
  };
}
