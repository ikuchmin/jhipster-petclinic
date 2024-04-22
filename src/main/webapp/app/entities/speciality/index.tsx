import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Speciality from './speciality';
import SpecialityDetail from './speciality-detail';
import SpecialityUpdate from './speciality-update';
import SpecialityDeleteDialog from './speciality-delete-dialog';

const SpecialityRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Speciality />} />
    <Route path="new" element={<SpecialityUpdate />} />
    <Route path=":id">
      <Route index element={<SpecialityDetail />} />
      <Route path="edit" element={<SpecialityUpdate />} />
      <Route path="delete" element={<SpecialityDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SpecialityRoutes;
