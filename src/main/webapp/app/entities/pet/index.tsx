import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Pet from './pet';
import PetDetail from './pet-detail';
import PetUpdate from './pet-update';
import PetDeleteDialog from './pet-delete-dialog';

const PetRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Pet />} />
    <Route path="new" element={<PetUpdate />} />
    <Route path=":id">
      <Route index element={<PetDetail />} />
      <Route path="edit" element={<PetUpdate />} />
      <Route path="delete" element={<PetDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PetRoutes;
