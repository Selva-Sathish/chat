import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css';
import { RouterProvider } from 'react-router-dom';
import routes from './router';
import {AuthProvider} from './context/AuthContext'
import AuthInitilizer from './auth/AuthInitializer';
createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <AuthProvider>
      <AuthInitilizer />
      <RouterProvider router={routes} />
    </AuthProvider>
  </StrictMode>
)
