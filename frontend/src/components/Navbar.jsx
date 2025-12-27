import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useTranslation } from 'react-i18next';

function Navbar() {
    const { user, isAuthenticated, logout } = useAuth();
    const { t, i18n } = useTranslation();
    const navigate = useNavigate();

    const changeLanguage = (lng) => {
        i18n.changeLanguage(lng);
    };

    const handleLogout = () => {
        logout();
        navigate('/');
    };

    return (
        <nav className="navbar">
            <div className="nav-container">
                <Link to="/" className="nav-logo">✈️ SkyBooker</Link>
                <ul className="nav-links">
                    <li><Link to="/">{t('navbar.search')}</Link></li>
                    {isAuthenticated && <li><Link to="/bookings">{t('navbar.bookings')}</Link></li>}
                </ul>
                <div className="nav-actions">
                    <div className="lang-toggle">
                        <button
                            className={i18n.language === 'pl' ? 'active' : ''}
                            onClick={() => changeLanguage('pl')}
                        >PL</button>
                        <button
                            className={i18n.language === 'en' ? 'active' : ''}
                            onClick={() => changeLanguage('en')}
                        >EN</button>
                    </div>
                    {isAuthenticated ? (
                        <div className="user-menu">
                            <span className="username">{user?.username}</span>
                            <button onClick={handleLogout} className="btn-logout">{t('navbar.logout')}</button>
                        </div>
                    ) : (
                        <div className="auth-links">
                            <Link to="/login" className="btn-login">{t('navbar.login')}</Link>
                            <Link to="/register" className="btn-register">{t('navbar.register')}</Link>
                        </div>
                    )}
                </div>
            </div>
        </nav>
    );
}

export default Navbar;
