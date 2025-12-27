import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'
import { AuthProvider } from './context/AuthContext'
import './i18n'
import Navbar from './components/Navbar'
import SearchPage from './pages/SearchPage'
import FlightDetailsPage from './pages/FlightDetailsPage'
import BookingFormPage from './pages/BookingFormPage'
import PaymentPage from './pages/PaymentPage'
import BookingsListPage from './pages/BookingsListPage'
import LoginPage from './pages/LoginPage'
import RegisterPage from './pages/RegisterPage'

function App() {
    return (
        <AuthProvider>
            <Router>
                <Navbar />
                <div className="container">
                    <Routes>
                        <Route path="/" element={<SearchPage />} />
                        <Route path="/login" element={<LoginPage />} />
                        <Route path="/register" element={<RegisterPage />} />
                        <Route path="/flights/:id" element={<FlightDetailsPage />} />
                        <Route path="/bookings/new" element={<BookingFormPage />} />
                        <Route path="/payment" element={<PaymentPage />} />
                        <Route path="/bookings" element={<BookingsListPage />} />
                    </Routes>
                </div>
            </Router>
        </AuthProvider>
    )
}

export default App
