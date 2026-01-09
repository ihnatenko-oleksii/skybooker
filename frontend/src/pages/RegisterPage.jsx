import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import axios from 'axios';
import { useTranslation } from 'react-i18next';

function RegisterPage() {
    const { t } = useTranslation();
    const [formData, setFormData] = useState({
        firstName: '',
        lastName: '',
        login: '',
        email: '',
        password: '',
        phone: ''
    });
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');
    const navigate = useNavigate();

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await axios.post('/api/auth/register', formData);
            setSuccess(t('auth.successRegister'));
            setTimeout(() => navigate('/login'), 2000);
        } catch (err) {
            setError(err.response?.data || 'Registration failed');
        }
    };

    return (
        <div className="auth-container">
            <h1>{t('auth.registerTitle')}</h1>
            <form onSubmit={handleSubmit} className="auth-form">
                <div className="form-grid">
                    <div className="form-group">
                        <label>{t('auth.firstName')}</label>
                        <input name="firstName" type="text" onChange={handleChange} required />
                    </div>
                    <div className="form-group">
                        <label>{t('auth.lastName')}</label>
                        <input name="lastName" type="text" onChange={handleChange} required />
                    </div>
                    <div className="form-group">
                        <label>{t('auth.username')}</label>
                        <input name="login" type="text" onChange={handleChange} required />
                    </div>
                    <div className="form-group">
                        <label>{t('auth.email')}</label>
                        <input name="email" type="email" onChange={handleChange} required />
                    </div>
                    <div className="form-group">
                        <label>{t('auth.password')}</label>
                        <input name="password" type="password" onChange={handleChange} required />
                    </div>
                    <div className="form-group">
                        <label>{t('auth.phone')}</label>
                        <input name="phone" type="text" onChange={handleChange} />
                    </div>
                </div>
                {error && <p className="error-message">{error}</p>}
                {success && <p className="success-message">{success}</p>}
                <button type="submit" className="btn btn-primary">{t('auth.registerTitle')}</button>
            </form>
            <p className="auth-footer">
                {t('navbar.login')}? <Link to="/login">{t('auth.loginTitle')}</Link>
            </p>
        </div>
    );
}

export default RegisterPage;
