import React from 'react';
import { Button } from 'react-native-elements';
export default (props) => <Button {...props} buttonStyle={[{ borderRadius: 20 }, props.buttonStyle]} />