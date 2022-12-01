import React from 'react';
import Header from './components/Header'
import Nav from './components/Nav'
import {useState, useEffect} from 'react'

function App() {
  return (
    <div class="App">
      <Header/>
      <hr></hr>
      <Nav></Nav>
    </div>
  );
}

export default App;
