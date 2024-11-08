import React from 'react';
import { Button, StyleSheet, Text, View } from 'react-native';




export default class Camera extends React.Component {
  render() {
    return (
      <View style={styles.container}>
      <Text style={styles.title}>Matematik Merdivenleri</Text>
      <Button
        title="Oyuna Başla"
        onPress={() => navigation.navigate('Game')}
      />
    </View>
    );
  }
}

const styles = StyleSheet.create({
  container: { flex: 1, justifyContent: 'center', alignItems: 'center' },
  title: { fontSize: 24, marginBottom: 20 },
});