import React from 'react';
import { ExpoConfigView } from '@expo/samples';
import LogoTitle from "../components/LogoTitle";

export default function SettingsScreen() {
  /**
   * Go ahead and delete ExpoConfigView and replace it with your content;
   * we just wanted to give you a quick view of your config.
   */
  return <ExpoConfigView />;
}

SettingsScreen.navigationOptions = {
  headerTitle: <LogoTitle title="Settings"/>
};
