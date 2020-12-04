import React from "react";
import { Route, BrowserRouter as Router } from "react-router-dom";
import Table from "./components/Table";

function App() {
  return (
  <Router>
    <Route exact path="/" component={Table} />
  </Router>
);
}

export default App;
