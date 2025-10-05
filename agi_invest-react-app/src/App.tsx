import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Home from './App/Home/Home';
import Login from './App/Login/Login';
import Signup from './App/Signup/Signup';
import Extract from './App/Extrato/Extract'; // ✅ importar Extract
import './App.css';

function App() {
  return (
    <div className="app-container">
      <Router>
        <Routes>
          <Route path="/" element={<Signup />} />
          <Route path="/login" element={<Login />} />
          <Route path="/signup" element={<Signup />} />
          <Route path="/home" element={<Home />} />
          <Route path="/extrato" element={<Extract />} /> {/* ✅ nova rota */}
        </Routes>
      </Router>
    </div>
  );
}

export default App;

