import React, { useState, useEffect, useRef } from 'react';
import { useTranslation } from 'react-i18next';
import { airportApi } from '../api/apiClient';
import './AirportSelector.css';

const AirportSelector = ({ label, value, onChange, placeholder, icon }) => {
    const { t } = useTranslation();
    const [airports, setAirports] = useState([]);
    const [searchTerm, setSearchTerm] = useState('');
    const [isOpen, setIsOpen] = useState(false);
    const [loading, setLoading] = useState(true);
    const wrapperRef = useRef(null);

    useEffect(() => {
        const fetchAirports = async () => {
            try {
                const response = await airportApi.getAllAirports();
                setAirports(response.data);
            } catch (error) {
                console.error("Failed to fetch airports", error);
            } finally {
                setLoading(false);
            }
        };
        fetchAirports();
    }, []);

    useEffect(() => {
        function handleClickOutside(event) {
            if (wrapperRef.current && !wrapperRef.current.contains(event.target)) {
                setIsOpen(false);
            }
        }
        document.addEventListener("mousedown", handleClickOutside);
        return () => {
            document.removeEventListener("mousedown", handleClickOutside);
        };
    }, [wrapperRef]);

    useEffect(() => {
        if (value && airports.length > 0) {
            const selected = airports.find(a => a.code === value);
            if (selected && !isOpen) {
                setSearchTerm(`${selected.city} (${selected.code})`);
            }
            if (!value && !isOpen) {
                setSearchTerm('');
            }
        }
    }, [value, airports, isOpen]);


    const filteredAirports = airports.filter(airport => {
        const term = searchTerm.toLowerCase();
        return airport.name.toLowerCase().includes(term) ||
            airport.city.toLowerCase().includes(term) ||
            airport.code.toLowerCase().includes(term) ||
            airport.country.toLowerCase().includes(term);
    });

    const handleSelect = (airport) => {
        onChange(airport.code);
        setSearchTerm(`${airport.city} (${airport.code})`);
        setIsOpen(false);
    };

    const handleInputChange = (e) => {
        setSearchTerm(e.target.value);
        setIsOpen(true);
        if (e.target.value === '') {
            onChange('');
        }
    };

    const handleFocus = () => {
        setIsOpen(true);
    };

    return (
        <div className="airport-selector" ref={wrapperRef}>
            <label>{label}</label>
            <div className="airport-input-wrapper">
                <span className="airport-icon">{icon || '✈️'}</span>
                <input
                    type="text"
                    className="airport-input"
                    placeholder={placeholder || t('search.fromPlaceholder')}
                    value={searchTerm}
                    onChange={handleInputChange}
                    onFocus={handleFocus}
                />

                {isOpen && (
                    <div className="airport-dropdown">
                        {loading ? (
                            <div className="no-airports">{t('search.loadingAirports')}</div>
                        ) : filteredAirports.length > 0 ? (
                            filteredAirports.map(airport => (
                                <div
                                    key={airport.code}
                                    className={`airport-item ${airport.code === value ? 'selected' : ''}`}
                                    onClick={() => handleSelect(airport)}
                                >
                                    <div className="airport-info">
                                        <span className="airport-city">{airport.city}, {airport.country}</span>
                                        <span className="airport-name">{airport.name}</span>
                                    </div>
                                    <span className="airport-code">{airport.code}</span>
                                </div>
                            ))
                        ) : (
                            <div className="no-airports">{t('search.noAirportsFound')}</div>
                        )}
                    </div>
                )}
            </div>
        </div>
    );
};

export default AirportSelector;
