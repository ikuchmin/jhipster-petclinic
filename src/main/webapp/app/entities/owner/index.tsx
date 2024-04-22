import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Owner from './owner';
import OwnerDetail from './owner-detail';
import OwnerUpdate from './owner-update';
import OwnerDeleteDialog from './owner-delete-dialog';

const OwnerRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Owner />} />
    <Route path="new" element={<OwnerUpdate />} />
    <Route path=":id">
      <Route index element={<OwnerDetail />} />
      <Route path="edit" element={<OwnerUpdate />} />
      <Route path="delete" element={<OwnerDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default OwnerRoutes;
