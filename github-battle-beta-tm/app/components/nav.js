import React from 'react';
import { ThemeConsumer } from '../contexts/theme'

export default function Nav() {
    return (
        <ThemeConsumer>
            {({ theme, toggleTheme }) => (
                <nav className='row space-between'>
                    <button className='btn-clear'
                        style={{ fontSize: 30 }}
                        onClick={toggleTheme} >
                        {
                            theme === 'light' ? '🔦' : '💡'
                        }
                    </button>
                </nav>
            )}
        </ThemeConsumer>
    )
}