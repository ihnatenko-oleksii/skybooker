import React, { useState, useEffect } from 'react';
import { useNavigate, Link, useSearchParams } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import axios from 'axios';
import { useTranslation } from 'react-i18next';

function LoginPage() {
    const { t } = useTranslation();
    const [loginName, setLoginName] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const { login, isAuthenticated } = useAuth();
    const navigate = useNavigate();
    const [searchParams] = useSearchParams();
    const redirect = searchParams.get('redirect');

    // If already authenticated, redirect to the intended destination or home
    useEffect(() => {
        if (isAuthenticated) {
            if (redirect) {
                navigate(redirect);
            } else {
                navigate('/');
            }
        }
    }, [isAuthenticated, redirect, navigate]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post('/api/auth/login', {
                login: loginName,
                password
            });
            login(response.data.accessToken);
            // Navigate after login - the useEffect will handle it if authenticated
            if (redirect) {
                navigate(redirect);
            } else {
                navigate('/');
            }
        } catch (err) {
            setError(err.response?.data?.message || 'Invalid credentials');
        }
    };

    return (
        <div className="auth-container">
            <h1>{t('auth.loginTitle')}</h1>
            <form onSubmit={handleSubmit} className="auth-form">
                <div className="form-group">
                    <label>{t('auth.username')}</label>
                    <input
                        type="text"
                        value={loginName}
                        onChange={(e) => setLoginName(e.target.value)}
                        required
                    />
                </div>
                <div className="form-group">
                    <label>{t('auth.password')}</label>
                    <input
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </div>
                {error && <p className="error-message">{error}</p>}
                <button type="submit" className="btn btn-primary">{t('auth.loginTitle')}</button>
            </form>
            <p className="auth-footer">
                {t('navbar.register')}? <Link to="/register">{t('auth.registerTitle')}</Link>
            </p>
        </div>
    );
}

export default LoginPage;
