package sk.styk.martin.apkanalyzer.activity.detailfragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sk.styk.martin.apkanalyzer.R;
import sk.styk.martin.apkanalyzer.activity.AppDetailFragment;
import sk.styk.martin.apkanalyzer.model.detail.CertificateData;
import sk.styk.martin.apkanalyzer.view.DetailItemView;

/**
 * Created by Martin Styk on 22.06.2017.
 */
public class AppDetailFragment_Certificate extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_app_detail_certificate, container, false);

        CertificateData data = getArguments().getParcelable(AppDetailFragment.ARG_CHILD);

        ((DetailItemView) rootView.findViewById(R.id.item_sign_algorithm)).setValue(data.getSignAlgorithm());

        String startDate = DateUtils.formatDateTime(getActivity(), data.getStartDate().getTime(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_TIME);
        ((DetailItemView) rootView.findViewById(R.id.item_start_date)).setValue(startDate);

        String endDate = DateUtils.formatDateTime(getActivity(), data.getEndDate().getTime(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_TIME);
        ((DetailItemView) rootView.findViewById(R.id.item_end_date)).setValue(endDate);

        ((DetailItemView) rootView.findViewById(R.id.item_public_key_md5)).setValue(data.getPublicKeyMd5());
        ((DetailItemView) rootView.findViewById(R.id.item_cert_md5)).setValue(data.getCertMd5());
        ((DetailItemView) rootView.findViewById(R.id.item_serial_number)).setValue(String.valueOf(data.getSerialNumber()));
        ((DetailItemView) rootView.findViewById(R.id.item_issuer_name)).setValue(data.getIssuerName());
        ((DetailItemView) rootView.findViewById(R.id.item_issuer_organization)).setValue(String.valueOf(data.getIssuerOrganization()));
        ((DetailItemView) rootView.findViewById(R.id.item_issuer_country)).setValue(data.getIssuerCountry());
        ((DetailItemView) rootView.findViewById(R.id.item_subject_name)).setValue(String.valueOf(data.getSubjectName()));
        ((DetailItemView) rootView.findViewById(R.id.item_subject_organization)).setValue(data.getSubjectOrganization());
        ((DetailItemView) rootView.findViewById(R.id.item_subject_country)).setValue(data.getSubjectCountry());

        return rootView;
    }
}
